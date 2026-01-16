package com.buildwithrani.backend.order.controller;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.mapper.OrderMapper;
import com.buildwithrani.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1️⃣ Place order from cart
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(orderService.placeOrder(email));
    }

    // 2️⃣ Get logged-in user's orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(orderService.getMyOrders(email));
    }

    // 3️⃣ Get single order details
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, email));
    }
}
