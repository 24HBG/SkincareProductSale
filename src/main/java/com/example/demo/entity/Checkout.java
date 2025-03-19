package com.example.demo.entity;

import com.example.demo.enums.CheckoutStatus;
import com.example.demo.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public String address;
    public String phoneNumber;

    public double totalAmount;
    public double discount;
    public double finalAmount;

    @Enumerated(EnumType.STRING)
    public PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    public CheckoutStatus status;

    public LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;
}
