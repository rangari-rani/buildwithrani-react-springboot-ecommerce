package com.buildwithrani.backend.order.controller;

import com.buildwithrani.backend.common.dto.ApiResponse;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.dto.UpdateOrderStatusRequest;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.mapper.OrderMapper;
import com.buildwithrani.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order status updated successfully",
                        orderService.advanceOrderStatus(
                                orderId,
                                request.getOrderStatus()
                        )
                )
        );
    }
}
