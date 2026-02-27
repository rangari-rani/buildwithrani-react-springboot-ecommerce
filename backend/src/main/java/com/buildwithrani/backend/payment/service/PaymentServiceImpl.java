package com.buildwithrani.backend.payment.service;

import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.enums.PaymentStatus;
import com.buildwithrani.backend.order.repository.OrderRepository;
import com.buildwithrani.backend.payment.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

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
            return; // Already verified, do nothing
        }

        // Security check: ensure order IDs match
        if (!razorpayOrderId.equals(order.getRazorpayOrderId())) {
            throw new InvalidStateException("Razorpay order ID mismatch");
        }

        boolean verified =
                paymentGateway.verifyPayment(
                        razorpayOrderId,
                        razorpayPaymentId,
                        signature
                );

        if (!verified) {
            throw new InvalidStateException("Payment signature verification failed");
        }

        order.markPaymentSuccess(razorpayPaymentId, signature);
    }
}