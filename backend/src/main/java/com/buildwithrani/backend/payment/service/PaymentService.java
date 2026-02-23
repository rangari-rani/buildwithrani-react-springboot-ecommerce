package com.buildwithrani.backend.payment.service;

public interface PaymentService {

    String createPayment(Long orderId);

    void verifyPayment(
            Long orderId,
            String razorpayOrderId,
            String razorpayPaymentId,
            String signature
    );
}