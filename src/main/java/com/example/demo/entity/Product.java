package com.example.demo.entity;

import com.example.demo.enums.SkinTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id = 0;
    @Column(nullable = false)
    public String name;
    public String description;
    @Min(value = 0)
    public float price;
    @Min(value = 0)
    public int stock;
    public SkinTypeEnum skinType;
    public BigDecimal rating;
    public LocalDateTime createdAt;
    public LocalDateTime updateAt;
    public boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    Promotion promotion;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
            @JsonIgnore
    List<Review> reviews = new ArrayList<>();

}
