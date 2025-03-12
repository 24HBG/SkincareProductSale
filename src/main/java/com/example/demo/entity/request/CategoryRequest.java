package com.example.demo.entity.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryRequest {
    String name;
    public String description;
}
