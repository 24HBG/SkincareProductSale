package com.example.demo.api;

import com.example.demo.entity.Order;
import com.example.demo.entity.request.OrderRequest;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*") // Cho phép gọi API từ frontend (nếu cần)
public class OrderAPI {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<String> create(
            @Valid @RequestBody OrderRequest orderRequest,
            @RequestParam(required = false) Long discountId, // Discount có thể có hoặc không
            @RequestParam String paymentMethod // Bắt buộc truyền phương thức thanh toán
    ) throws Exception {
        String urlPayment = orderService.checkout(orderRequest, paymentMethod, discountId);
        return ResponseEntity.ok(urlPayment);
    }

    // ✅ API Cập nhật trạng thái đơn hàng
    @PatchMapping("/{id}")
    public ResponseEntity<Order> updateStatus(
            @PathVariable long id,
            @RequestParam OrderStatusEnum status
    ) {
        Order order = orderService.updateStatus(status, id);
        return ResponseEntity.ok(order);
    }

    // ✅ API Lấy tất cả đơn hàng
    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders);
    }

    // ✅ API Lấy đơn hàng của user hiện tại
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrdersByUser() {
        List<Order> orders = orderService.getOrdersByUser();
        return ResponseEntity.ok(orders);
    }

}
