package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String name;
    public String description;
    public LocalDateTime createdAt;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    public List<Product> products = new ArrayList<>();
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
