package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public double subtotal;
    public double discount;
    public double shippingFee;
    public double totalPrice;
    public boolean isCheckout = false;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnore
    List<CartItem> cartItems = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "account_id", unique = true)
    Account account;
}
