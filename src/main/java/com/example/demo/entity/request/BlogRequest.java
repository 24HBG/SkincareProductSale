package com.example.demo.entity.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class BlogRequest {
    public String title;
    public String content;
    public Long accountId;
}
