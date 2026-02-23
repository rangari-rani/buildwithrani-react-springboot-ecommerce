package com.buildwithrani.backend.payment.gateway;

import java.math.BigDecimal;

public interface PaymentGateway {

    String createOrder(BigDecimal amount);

    boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String signature
    );
}