package com.example.demo.api;

import com.example.demo.entity.Cart;
import com.example.demo.entity.request.CartRequest;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "api")
public class CartAPI {

    @Autowired
    CartService cartService;


    @PostMapping("/add")
    public Cart addToCart(@RequestBody CartRequest cartRequest) {
        return cartService.addToCart(cartRequest);
    }

    @GetMapping("/view")
    public ResponseEntity viewCart(){
        List<Cart> carts = cartService.get();
        return ResponseEntity.ok(carts);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeItem(@RequestBody CartRequest cartRequest){
        Cart carts = cartService.removeItem(cartRequest);
        return ResponseEntity.ok(carts);
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<Cart> updateItem(@PathVariable Long accountId, @RequestBody CartRequest cartRequest) {
        Cart updatedCart = cartService.updateItem(accountId, cartRequest);
        return ResponseEntity.ok(updatedCart);  // Trả về giỏ hàng đã được cập nhật
    }


}
