package com.buildwithrani.backend.auth.service;

import com.buildwithrani.backend.auth.dto.AuthResponse;
import com.buildwithrani.backend.auth.dto.LoginRequest;
import com.buildwithrani.backend.auth.dto.SignupRequest;
import com.buildwithrani.backend.auth.model.Role;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.auth.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    // ======================
    // SIGNUP
    // ======================

    @Test
    void shouldSignupSuccessfullyWithDefaultRole() {

        SignupRequest request = new SignupRequest();
        request.setFullName("Rani");
        request.setEmail("rani@test.com");
        request.setPassword("password");

        when(passwordEncoder.encode("password"))
                .thenReturn("encoded-password");

        when(jwtUtil.generateToken(any(), any(), anyInt()))
                .thenReturn("jwt-token");

        AuthResponse response = authService.signup(request);

        assertTrue(response.isSuccess());
        assertEquals("rani@test.com", response.getEmail());
        assertEquals("jwt-token", response.getToken());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password");
        verify(jwtUtil).generateToken(any(), any(), anyInt());
    }

    @Test
    void shouldSignupWithCustomRole() {

        SignupRequest request = new SignupRequest();
        request.setFullName("Admin");
        request.setEmail("admin@test.com");
        request.setPassword("password");
        request.setRole("ADMIN");

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(jwtUtil.generateToken(any(), any(), anyInt()))
                .thenReturn("token");

        AuthResponse response = authService.signup(request);

        assertTrue(response.isSuccess());
        verify(userRepository).save(argThat(user ->
                user.getRole() == Role.ADMIN
        ));
    }

    // ======================
    // LOGIN
    // ======================

    @Test
    void shouldLoginSuccessfully() {

        User user = new User();
        user.setEmail("rani@test.com");
        user.setPassword("encoded-password");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("rani@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encoded-password"))
                .thenReturn(true);

        when(jwtUtil.generateToken(any(), any(), anyInt()))
                .thenReturn("jwt-token");

        LoginRequest request = new LoginRequest();
        request.setEmail("rani@test.com");
        request.setPassword("password");

        AuthResponse response = authService.login(request);

        assertTrue(response.isSuccess());
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void shouldThrowWhenUserNotFound() {

        when(userRepository.findByEmail("rani@test.com"))
                .thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setEmail("rani@test.com");
        request.setPassword("password");

        assertThrows(RuntimeException.class,
                () -> authService.login(request));
    }

    @Test
    void shouldThrowWhenPasswordMismatch() {

        User user = new User();
        user.setEmail("rani@test.com");
        user.setPassword("encoded-password");

        when(userRepository.findByEmail("rani@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("rani@test.com");
        request.setPassword("wrong-password");

        assertThrows(RuntimeException.class,
                () -> authService.login(request));
    }

    // ======================
    // LOGOUT
    // ======================

    @Test
    void shouldLogoutAndIncrementTokenVersion() {

        User user = new User();
        user.setEmail("rani@test.com");

        when(userRepository.findByEmail("rani@test.com"))
                .thenReturn(Optional.of(user));

        authService.logout("rani@test.com");

        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenLogoutUserNotFound() {

        when(userRepository.findByEmail("rani@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.logout("rani@test.com"));
    }
}