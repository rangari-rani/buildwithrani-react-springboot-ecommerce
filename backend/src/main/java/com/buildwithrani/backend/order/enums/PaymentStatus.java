package com.buildwithrani.backend.order.enums;

public enum PaymentStatus {

    PENDING,     // order created in our system
    CREATED,     // razorpay order created
    SUCCESS,     // payment verified
    FAILED,
    REFUNDED
}
