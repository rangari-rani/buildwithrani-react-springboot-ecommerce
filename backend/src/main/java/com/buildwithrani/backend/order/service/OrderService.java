package com.buildwithrani.backend.order.service;

import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.entity.Order;

import java.util.List;

public interface OrderService {

    // ===== USER METHODS =====
    OrderResponse placeOrder(String email);

    List<OrderResponse> getMyOrders(String email);

    OrderResponse getOrderById(Long orderId, String email);

    // ===== ADMIN METHODS =====
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);

}
