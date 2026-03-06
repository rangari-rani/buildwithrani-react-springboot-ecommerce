package com.buildwithrani.backend.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreatePaymentRequest {

    @NotNull
    @Positive
    private Long orderId;
}