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
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String code; // Mã giảm giá

    public float discountValue; // Giá trị giảm (VNĐ)

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Order> orders = new ArrayList<>();
}
