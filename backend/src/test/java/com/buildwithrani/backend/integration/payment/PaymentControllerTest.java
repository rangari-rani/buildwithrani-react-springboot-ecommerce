package com.buildwithrani.backend.integration.payment;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.buildwithrani.backend.payment.controller.PaymentController;
import com.buildwithrani.backend.payment.dto.CreatePaymentRequest;
import com.buildwithrani.backend.payment.dto.VerifyPaymentRequest;
import com.buildwithrani.backend.payment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePaymentSuccessfully() throws Exception {

        CreatePaymentRequest request = new CreatePaymentRequest();

        ReflectionTestUtils.setField(request, "orderId", 1L);

        when(paymentService.createPayment(1L))
                .thenReturn("order_razorpay_123");

        mockMvc.perform(post("/api/payments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(paymentService).createPayment(1L);
    }

    @Test
    void shouldVerifyPaymentSuccessfully() throws Exception {

        VerifyPaymentRequest request = new VerifyPaymentRequest();

        ReflectionTestUtils.setField(request, "orderId", 1L);
        ReflectionTestUtils.setField(request, "razorpayOrderId", "order_123");
        ReflectionTestUtils.setField(request, "razorpayPaymentId", "payment_123");
        ReflectionTestUtils.setField(request, "signature", "signature_123");

        mockMvc.perform(post("/api/payments/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(paymentService).verifyPayment(
                1L,
                "order_123",
                "payment_123",
                "signature_123"
        );
    }

    @Test
    void shouldReturnBadRequestWhenOrderIdMissing() throws Exception {

        CreatePaymentRequest request = new CreatePaymentRequest();

        mockMvc.perform(post("/api/payments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}