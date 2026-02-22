package com.buildwithrani.backend.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private boolean success;
    private int status;
    private String errorCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}