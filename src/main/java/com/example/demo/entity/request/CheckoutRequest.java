package com.example.demo.entity.request;

import com.example.demo.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {

     public Long accountId;
     public String address;
     public String phoneNumber;
     public PaymentMethod paymentMethod;
     boolean applyDiscount; // ✅ Thêm biến này
}
