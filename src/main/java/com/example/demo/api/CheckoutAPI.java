package com.example.demo.api;

import com.example.demo.entity.Checkout;
import com.example.demo.entity.request.CheckoutRequest;
import com.example.demo.service.CheckoutService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@SecurityRequirement(name = "api")
public class CheckoutAPI {
    @Autowired
    CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
        Checkout checkout = checkoutService.createCheckout(request);
        return ResponseEntity.ok(checkout);
    }
    @GetMapping
    public ResponseEntity<List<Checkout>> getAll() {
        List<Checkout> checkouts = checkoutService.findAll();
        return ResponseEntity.ok(checkouts);
    }
}
