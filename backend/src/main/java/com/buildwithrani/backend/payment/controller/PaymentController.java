package com.buildwithrani.backend.payment.controller;

import com.buildwithrani.backend.common.dto.ApiResponse;
import com.buildwithrani.backend.payment.dto.CreatePaymentRequest;
import com.buildwithrani.backend.payment.dto.VerifyPaymentRequest;
import com.buildwithrani.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ApiResponse<String> createPayment(
            @RequestBody CreatePaymentRequest request
    ) {

        String razorpayOrderId =
                paymentService.createPayment(request.getOrderId());

        return ApiResponse.success("Razorpay order created", razorpayOrderId);
    }

    @PostMapping("/verify")
    public ApiResponse<Void> verifyPayment(
            @RequestBody VerifyPaymentRequest request
    ) {

        paymentService.verifyPayment(
                request.getOrderId(),
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getSignature()
        );

        return ApiResponse.success("Payment verified successfully");
    }
}