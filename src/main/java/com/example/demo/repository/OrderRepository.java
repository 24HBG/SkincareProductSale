package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByAccountId(long accountId);
    Order findOrderById(long id);


    List<Order> findByAccountAndStatus(Account account, OrderStatusEnum orderStatusEnum);
}
