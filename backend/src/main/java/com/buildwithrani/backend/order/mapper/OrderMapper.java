package com.buildwithrani.backend.order.mapper;

import com.buildwithrani.backend.order.dto.OrderItemResponse;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
        // utility class
    }

    public static OrderResponse toOrderResponse(Order order) {

        List<OrderItemResponse> itemResponses = order.getOrderItems()
                .stream()
                .map(OrderMapper::toOrderItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }

    private static OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .priceAtPurchase(item.getPriceAtPurchase())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
