package com.buildwithrani.backend.payment.dto;

import lombok.Getter;

@Getter
public class VerifyPaymentRequest {

    private Long orderId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String signature;
}