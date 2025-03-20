package com.example.demo.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    List<OrderDetailRequest> details;
    String paymentMethod;  // "VNPay", "Cash",...
    Long discountId;

    String phoneNumber;                // Số điện thoại khách hàng
    String customerAddress;
}
