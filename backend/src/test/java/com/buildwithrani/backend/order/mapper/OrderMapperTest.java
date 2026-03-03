package com.buildwithrani.backend.order.mapper;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.order.dto.OrderItemResponse;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


class OrderMapperTest {

    @Test
    void shouldMapOrderToOrderResponse() {

        User user = new User();
        user.setId(1L);

        Order order = Order.create(
                user,
                BigDecimal.valueOf(500),
                new ArrayList<>()
        );

        setId(order, 10L);

        OrderItem item = OrderItem.builder()
                .order(order)
                .productId(100L)
                .productName("Laptop")
                .priceAtPurchase(BigDecimal.valueOf(250))
                .quantity(2)
                .totalPrice(BigDecimal.valueOf(500))
                .build();

        order.getOrderItems().add(item);

        OrderResponse response = OrderMapper.toOrderResponse(order);

        assertEquals(10L, response.getOrderId());
        assertEquals(BigDecimal.valueOf(500), response.getTotalAmount());
        assertEquals(1, response.getItems().size());

        OrderItemResponse itemResponse = response.getItems().get(0);

        assertEquals(100L, itemResponse.getProductId());
        assertEquals("Laptop", itemResponse.getProductName());
        assertEquals(BigDecimal.valueOf(250), itemResponse.getPriceAtPurchase());
        assertEquals(2, itemResponse.getQuantity());
        assertEquals(BigDecimal.valueOf(500), itemResponse.getTotalPrice());
    }

    private void setId(Object entity, Long id) {
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}