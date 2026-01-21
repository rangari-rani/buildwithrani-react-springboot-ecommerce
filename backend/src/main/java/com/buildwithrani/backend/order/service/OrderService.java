package com.buildwithrani.backend.order.service;

import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    /* =====================
       USER ACTIONS
       ===================== */

    OrderResponse placeOrder(String email);

    List<OrderResponse> getMyOrders(String email);

    OrderResponse getOrderById(Long orderId, String email);

    OrderResponse cancelOrder(Long orderId, String email);

    /* =====================
       ADMIN ACTIONS
       ===================== */

    List<OrderResponse> getAllOrders();

    OrderResponse advanceOrderStatus(
            Long orderId,
            OrderStatus nextStatus
    );
}
