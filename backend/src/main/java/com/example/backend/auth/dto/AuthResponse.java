package com.example.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private boolean success;
    private String message;
    private String email;
    private String token;
}


