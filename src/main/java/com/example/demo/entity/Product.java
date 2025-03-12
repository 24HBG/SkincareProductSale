package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

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
    @Min(value = 0)
    public float price;
    @Min(value = 0)
    public int stock;
    public boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    List<OrderDetail> orderDetails = new ArrayList<>();

}
