package com.example.demo.entity;

import ch.qos.logback.core.model.INamedModel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public int rating;
    @Column(columnDefinition = "TEXT")
    public String comment;
    public LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
