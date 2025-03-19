package com.example.demo.repository;

import com.example.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    // Tìm CartItem theo CartId và ProductId
    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    // Tính tổng tiền (subtotal) cho một giỏ hàng (cartId)
    @Query("SELECT SUM(ci.price * ci.quantity) FROM CartItem ci WHERE ci.cart.id = :cartId")
    Double getSubtotalByCartId(@Param("cartId") Long cartId);
}
