package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DashBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String name;
    @Column(columnDefinition = "TEXT")
    public String description;
    public LocalDateTime createdAt;

    @PrePersist
    public void onCreated() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "createdBy")
    Account createdBy;

    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    List<Report> reports = new ArrayList<>();

}
