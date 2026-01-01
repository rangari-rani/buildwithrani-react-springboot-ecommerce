package com.buildwithrani.backend.auth.controller;

import com.buildwithrani.backend.auth.dto.AuthResponse;
import com.buildwithrani.backend.auth.dto.LoginRequest;
import com.buildwithrani.backend.auth.dto.SignupRequest;
import com.buildwithrani.backend.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @PostMapping("/signup")
    public AuthResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

}
