package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id = 0;
    public Integer quantity;
    public Double price;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
