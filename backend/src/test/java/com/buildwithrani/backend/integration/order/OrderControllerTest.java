package com.buildwithrani.backend.integration.order;

import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {

        when(orderService.placeOrder(null))
                .thenReturn(new OrderResponse());

        mockMvc.perform(post("/api/orders"))
                .andExpect(status().isOk());

        verify(orderService).placeOrder(null);
    }

    @Test
    void shouldReturnMyOrders() throws Exception {

        when(orderService.getMyOrders(null))
                .thenReturn(List.of(new OrderResponse()));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());

        verify(orderService).getMyOrders(null);
    }

    @Test
    void shouldReturnOrderById() throws Exception {

        when(orderService.getOrderById(1L, null))
                .thenReturn(new OrderResponse());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk());

        verify(orderService).getOrderById(1L, null);
    }

    @Test
    void shouldCancelOrderSuccessfully() throws Exception {

        when(orderService.cancelOrder(1L, null))
                .thenReturn(new OrderResponse());

        mockMvc.perform(patch("/api/orders/1/cancel"))
                .andExpect(status().isOk());

        verify(orderService).cancelOrder(1L, null);
    }
}
