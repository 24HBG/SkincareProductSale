package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
//@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String name;
    @Column(columnDefinition = "TEXT")
    public String data;
    public LocalDateTime createdAt;

    @PrePersist
    public void onCreated() {
        this.createdAt = LocalDateTime.now();
    }
    @ManyToOne
    @JoinColumn(name = "dashboard_id")
    DashBoard dashboard;
}
