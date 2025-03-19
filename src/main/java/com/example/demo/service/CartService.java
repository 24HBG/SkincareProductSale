package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Cart;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.CartRequest;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartItemRepository cartItemRepository;


    public Cart addToCart(CartRequest cartRequest) {
        // Tìm tài khoản của user
        Account account = authenticationRepository.findById(cartRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm giỏ hàng theo account (hoặc tạo mới nếu chưa có)
        Cart cart = cartRepository.findByAccount(account);
        if (cart == null) {
            cart = new Cart();
            cart.setAccount(account);
            cart = cartRepository.save(cart); // Lưu giỏ hàng mới
        }

        // Kiểm tra sản phẩm có tồn tại không
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Kiểm tra số lượng sản phẩm trong kho
        if (cartRequest.getQuantity() > product.getStock()) {
            throw new RuntimeException("Product is out of stock");
        } else {
            // Nếu số lượng yêu cầu có sẵn trong kho, tiếp tục thêm vào giỏ hàng
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

            // Nếu sản phẩm chưa có trong giỏ hàng, tạo mới
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(cartRequest.getQuantity());
                cartItem.setPrice(product.getPrice() * cartRequest.getQuantity()); // Tính tổng tiền sản phẩm
                cart.getCartItems().add(cartItem);  // Thêm CartItem vào giỏ hàng
            } else {
                // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
                cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
                cartItem.setPrice(product.getPrice() * cartItem.getQuantity()); // Cập nhật lại giá
            }

            // Giảm số lượng sản phẩm trong kho
            product.setStock(product.getStock() - cartRequest.getQuantity());
            productRepository.save(product);  // Lưu thông tin sản phẩm với số lượng đã giảm

            // Lưu lại CartItem mới hoặc đã cập nhật
            cartItemRepository.save(cartItem);
        }

        // Cập nhật tổng tiền giỏ hàng
        double subtotal = cartItemRepository.getSubtotalByCartId(cart.getId());
        cart.setSubtotal(subtotal); // Cập nhật subtotal
        cart.setTotalPrice(subtotal + cart.getShippingFee() - cart.getDiscount()); // Tính tổng tiền

        // Lưu giỏ hàng đã cập nhật
        return cartRepository.save(cart);
    }

    public List<Cart> get() {
        return cartRepository.findAll();
    }

    public Cart removeItem(CartRequest cartRequest) {
        // Tìm giỏ hàng của user
        Account account = authenticationRepository.findById(cartRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByAccount(account);
        if (cart == null) {
            throw new RuntimeException("Cart not found for this user");
        }

        // Tìm sản phẩm trong giỏ hàng
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), cartRequest.getProductId());
        if (cartItem != null) {
            // Nếu sản phẩm có trong giỏ hàng, xóa nó
            cartItemRepository.delete(cartItem);

            // Cập nhật lại tổng tiền giỏ hàng sau khi xóa sản phẩm
            Double subtotal = cartItemRepository.getSubtotalByCartId(cart.getId());
            cart.setSubtotal(subtotal); // Cập nhật subtotal
            cart.setTotalPrice(subtotal + cart.getShippingFee() - cart.getDiscount()); // Tính lại tổng tiền giỏ hàng
            cartRepository.save(cart); // Lưu giỏ hàng đã cập nhật
        } else {
            throw new RuntimeException("Product not found in cart");
        }

        return cart;
    }


    public Cart updateItem(Long accountId, CartRequest cartRequest) {
        // Tìm tài khoản người dùng từ accountId
        Account account = authenticationRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm giỏ hàng của người dùng
        Cart cart = cartRepository.findByAccount(account);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        // Tìm sản phẩm trong giỏ hàng
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), cartRequest.getProductId());
        if (cartItem == null) {
            throw new RuntimeException("Product not found in cart");
        }

        // Tìm sản phẩm trong kho
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Kiểm tra số lượng sản phẩm trong kho
        if (cartRequest.getQuantity() > product.getStock() + cartItem.getQuantity()) {
            throw new RuntimeException("Not enough stock");
        }

        // Tính số lượng sản phẩm đã thay đổi và cập nhật lại số lượng trong kho
        int previousQuantity = cartItem.getQuantity();
        int newQuantity = cartRequest.getQuantity();
        int stockDifference = previousQuantity - newQuantity;  // Số sản phẩm dư nếu quantity giảm

        if (stockDifference > 0) {
            // Nếu số lượng giảm, trả lại số sản phẩm dư vào kho
            product.setStock(product.getStock() + stockDifference);
        } else if (stockDifference < 0) {
            // Nếu số lượng tăng, giảm số lượng trong kho
            int increaseInStock = Math.abs(stockDifference);
            if (product.getStock() < increaseInStock) {
                throw new RuntimeException("Not enough stock");
            }
            product.setStock(product.getStock() - increaseInStock);
        }

        // Cập nhật số lượng và giá sản phẩm trong giỏ hàng
        cartItem.setQuantity(newQuantity);
        cartItem.setPrice(product.getPrice() * newQuantity);

        // Lưu lại CartItem đã được cập nhật
        cartItemRepository.save(cartItem);

        // Cập nhật lại tổng tiền giỏ hàng
        double subtotal = cartItemRepository.getSubtotalByCartId(cart.getId());
        cart.setSubtotal(subtotal);

        // Cập nhật lại tổng tiền giỏ hàng (bao gồm phí vận chuyển và giảm giá)
        double totalPrice = subtotal + cart.getShippingFee() - cart.getDiscount();
        cart.setTotalPrice(totalPrice);

        // Lưu lại giỏ hàng đã được cập nhật
        productRepository.save(product); // Lưu lại kho sản phẩm
        return cartRepository.save(cart);
    }
}

