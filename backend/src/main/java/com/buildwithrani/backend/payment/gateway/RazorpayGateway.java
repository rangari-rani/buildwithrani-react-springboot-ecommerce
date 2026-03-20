package com.buildwithrani.backend.payment.gateway;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RazorpayGateway implements PaymentGateway {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Value("${razorpay.webhook.secret:default_secret}")
    private String webhookSecret;

    public RazorpayGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String createOrder(BigDecimal amount) {
        try {
            JSONObject orderRequest = new JSONObject();

            // Razorpay expects amount in paise
            orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());
            orderRequest.put("payment_capture", 1);

            Order order = razorpayClient.orders.create(orderRequest);

            return order.get("id");

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        try {
            return Utils.verifyWebhookSignature(payload, signature, webhookSecret);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String signature
    ) {
        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;

            return Utils.verifySignature(
                    payload,
                    signature,
                    razorpaySecret
            );

        } catch (Exception e) {
            return false;
        }
    }
}