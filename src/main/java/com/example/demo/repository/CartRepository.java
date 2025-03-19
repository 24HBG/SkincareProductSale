package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByAccountId(Long id);

    Cart findByAccount(Account account);
}
