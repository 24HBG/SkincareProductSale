package com.example.demo.entity.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private int rating;
    private String comment;
    private long accountId;
    private long productId;
}
