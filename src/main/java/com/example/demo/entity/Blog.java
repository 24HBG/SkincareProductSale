package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String title;
    @Column(columnDefinition = "TEXT")
    public String content;
    public LocalDateTime createdAt;
    public boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "account_id")
    public Account account;

}
