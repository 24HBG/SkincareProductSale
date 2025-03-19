package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Checkout;
import com.example.demo.entity.Order;
import com.example.demo.entity.request.CheckoutRequest;
import com.example.demo.enums.CheckoutStatus;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.repository.CheckoutRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckoutService {
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CheckoutRepository checkoutRepository;

    public Checkout createCheckout(CheckoutRequest request) {
        Account account = authenticationRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getAddress() == null || request.getPhoneNumber() == null) {
            throw new RuntimeException("Address and Phone Number are required");
        }

        List<Order> orders = orderRepository.findByAccountAndStatus(account, OrderStatusEnum.IN_PROCESS);
        double totalAmount = orders.stream().mapToDouble(Order::getTotal).sum();

        // ✅ Nếu người dùng chọn discount thì mới áp dụng giảm giá
        double discount = request.isApplyDiscount() ? calculateDiscount(account, totalAmount) : 0;
        double finalAmount = totalAmount - discount;

        Checkout checkout = new Checkout();
        checkout.setAccount(account);
        checkout.setAddress(request.getAddress());
        checkout.setPhoneNumber(request.getPhoneNumber());
        checkout.setTotalAmount(totalAmount);
        checkout.setDiscount(discount);
        checkout.setFinalAmount(finalAmount);
        checkout.setPaymentMethod(request.getPaymentMethod());
        checkout.setStatus(CheckoutStatus.PENDING);
        checkout.setCreatedAt(LocalDateTime.now());

        return checkoutRepository.save(checkout);
    }
    private double calculateDiscount(Account account, double totalAmount) {
        return 0; // Ví dụ: giảm 10%
    }


    public List<Checkout> findAll() {
        return checkoutRepository.findAll();
    }
}
