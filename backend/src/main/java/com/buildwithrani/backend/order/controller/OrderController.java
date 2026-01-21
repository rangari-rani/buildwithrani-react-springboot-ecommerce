package com.buildwithrani.backend.order.controller;

import com.buildwithrani.backend.auth.security.SecurityUtils;
import com.buildwithrani.backend.common.dto.ApiResponse;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //  Place order from cart
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(orderService.placeOrder(email));
    }

    //  Get logged-in user's orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(orderService.getMyOrders(email));
    }

    //  Get single order details
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, email));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long orderId
    ) {
        String userEmail = SecurityUtils.getCurrentUserEmail();

        OrderResponse response =
                orderService.cancelOrder(orderId, userEmail);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order cancelled successfully",
                        response
                )
        );
    }
}
