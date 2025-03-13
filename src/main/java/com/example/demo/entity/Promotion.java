package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public BigDecimal discountPercent;
    public LocalDateTime starAt;
    public LocalDateTime endAt;
    @Column(columnDefinition = "TEXT")
    public String description;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    List<Product> products = new ArrayList<>();
}
