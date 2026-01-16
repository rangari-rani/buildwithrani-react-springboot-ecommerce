package com.buildwithrani.backend.order.dto;

import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
