package com.buildwithrani.backend.payment.service;

import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;
import com.buildwithrani.backend.order.enums.PaymentStatus;
import com.buildwithrani.backend.order.repository.OrderRepository;
import com.buildwithrani.backend.payment.gateway.PaymentGateway;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    public String createPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Prevent duplicate payment creation
        if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new InvalidStateException("Order is already paid");
        }

        if (order.getPaymentStatus() == PaymentStatus.CREATED) {
            return order.getRazorpayOrderId(); // idempotent behavior
        }

        String razorpayOrderId =
                paymentGateway.createOrder(order.getTotalAmount());

        order.markPaymentCreated(razorpayOrderId);

        return razorpayOrderId;
    }

    @Override
    public void processWebhook(String payload, String signature) {
        if (!paymentGateway.verifyWebhookSignature(payload, signature)) {
            throw new SecurityException("Invalid webhook signature");
        }

        String razorpayOrderId = parseOrderIdFromPayload(payload);

        Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getPaymentStatus() == PaymentStatus.SUCCESS) return;

        // Common logic: Reduce stock then mark success
        finalizeOrder(order, "WEBHOOK_RECOVERY", signature);
    }

    @Override
    public void verifyPayment(
            Long orderId,
            String razorpayOrderId,
            String razorpayPaymentId,
            String signature
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Idempotency guard
        if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        if (!razorpayOrderId.equals(order.getRazorpayOrderId())) {
            throw new InvalidStateException("Razorpay order ID mismatch");
        }

        boolean verified = paymentGateway.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                signature
        );

        if (!verified) {
            throw new InvalidStateException("Payment signature verification failed");
        }

        // Reduce stock FIRST
        for (OrderItem item : order.getOrderItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            product.reduceStock(item.getQuantity());
        }

        //  Then mark payment success
        order.markPaymentSuccess(razorpayPaymentId, signature);
    }

    private void finalizeOrder(Order order, String paymentId, String signature) {
        // Reduce stock
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            product.reduceStock(item.getQuantity());
        }
        // Mark success
        order.markPaymentSuccess(paymentId, signature);
    }

    private String parseOrderIdFromPayload(String payload) {
        try {
            return objectMapper.readTree(payload).at("/payload/payment/entity/order_id").asText();
        } catch (Exception e) {
            throw new RuntimeException("Json parsing failed", e);
        }
    }
}