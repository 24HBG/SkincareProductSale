package com.example.demo.entity;

import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id = 0;

    public LocalDateTime createAt = LocalDateTime.now();

    public float total;

    public OrderStatusEnum status = OrderStatusEnum.IN_PROCESS;

    @ManyToOne
    @JoinColumn(name = "account_id")
    public Account account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetail> orderDetails = new ArrayList<>();

    // ✅ Thêm thông tin khách hàng
    @Column(nullable = false)
    public String phoneNumber;   // Bắt buộc nhập số điện thoại

    @Column(nullable = false)
    public String customerAddress; // Bắt buộc nhập địa chỉ

    // ✅ Thông tin thanh toán
    @Column(nullable = false)
    public String paymentMethod;  // "VNPay", "Cash",...
    @Enumerated(EnumType.STRING)
    public PaymentStatus paymentStatus; // false: chưa thanh toán, true: đã thanh toán


    // ✅ Thêm phí vận chuyển
    public float shippingFee = 30000; // Default shipping fee (có thể thay đổi sau)

    // ✅ Giảm giá (có thể null nếu không có discount)
    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = true)
    public Discount discount;

    public float discountValue = 0; // Mặc định không có giảm giá
}
