package com.example.demo.enums;

public enum PaymentStatus {
    PENDING, // Chờ thanh toán (COD hoặc VNPay chưa hoàn tất)
    PAID,    // Đã thanh toán thành công
    FAILED   // Thanh toán thất bại
}
