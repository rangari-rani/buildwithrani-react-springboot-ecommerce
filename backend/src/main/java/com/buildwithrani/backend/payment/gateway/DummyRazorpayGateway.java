package com.buildwithrani.backend.payment.gateway;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DummyRazorpayGateway implements PaymentGateway {

    @Override
    public String createOrder(BigDecimal amount) {

        // simulate razorpay order id
        return "rzp_order_" + UUID.randomUUID();
    }

    @Override
    public boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String signature
    ) {

        // Dummy logic:
        // If all fields exist → success
        return razorpayOrderId != null
                && razorpayPaymentId != null
                && signature != null;
    }
}