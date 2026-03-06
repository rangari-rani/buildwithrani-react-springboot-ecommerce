package com.buildwithrani.backend.unit.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import com.buildwithrani.backend.payment.gateway.RazorpayGateway;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.razorpay.Order;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;

class RazorpayGatewayTest {

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {

        RazorpayClient razorpayClient = mock(RazorpayClient.class);
        OrderClient orderClient = mock(OrderClient.class);
        Order order = mock(Order.class);

        razorpayClient.orders = orderClient;

        when(orderClient.create(any(JSONObject.class))).thenReturn(order);
        when(order.get("id")).thenReturn("order_123");

        RazorpayGateway gateway = new RazorpayGateway(razorpayClient);

        String result = gateway.createOrder(BigDecimal.valueOf(500));

        assertEquals("order_123", result);
    }

    @Test
    void shouldConvertAmountToPaise() throws Exception {

        RazorpayClient razorpayClient = mock(RazorpayClient.class);
        OrderClient orderClient = mock(OrderClient.class);
        Order order = mock(Order.class);

        razorpayClient.orders = orderClient;

        when(orderClient.create(any(JSONObject.class))).thenReturn(order);
        when(order.get("id")).thenReturn("order_123");

        RazorpayGateway gateway = new RazorpayGateway(razorpayClient);

        gateway.createOrder(BigDecimal.valueOf(500));

        verify(orderClient).create(argThat(json ->
                json.getInt("amount") == 50000
        ));
    }

    @Test
    void shouldThrowExceptionWhenRazorpayFails() throws Exception {

        RazorpayClient razorpayClient = mock(RazorpayClient.class);
        OrderClient orderClient = mock(OrderClient.class);

        razorpayClient.orders = orderClient;

        when(orderClient.create(any(JSONObject.class)))
                .thenThrow(new RuntimeException());

        RazorpayGateway gateway = new RazorpayGateway(razorpayClient);

        assertThrows(RuntimeException.class,
                () -> gateway.createOrder(BigDecimal.valueOf(500)));
    }

    @Test
    void shouldReturnFalseWhenVerificationFails() {

        RazorpayGateway gateway = new RazorpayGateway(null);

        boolean result = gateway.verifyPayment(
                "order_1",
                "payment_1",
                "invalid_signature"
        );

        assertFalse(result);
    }
}