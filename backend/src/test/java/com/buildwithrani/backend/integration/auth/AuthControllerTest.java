package com.buildwithrani.backend.integration.auth;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.buildwithrani.backend.auth.dto.AuthResponse;
import com.buildwithrani.backend.auth.dto.LoginRequest;
import com.buildwithrani.backend.auth.dto.SignupRequest;
import com.buildwithrani.backend.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest();
        ReflectionTestUtils.setField(request, "email", "test@example.com");
        ReflectionTestUtils.setField(request, "password", "password");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse(
                        true,
                        "Login successful",
                        "test@example.com",
                        "jwt-token"
                ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void shouldSignupSuccessfully() throws Exception {

        SignupRequest request = new SignupRequest();
        request.setFullName("John Doe");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setRole("USER");

        when(authService.signup(any(SignupRequest.class)))
                .thenReturn(new AuthResponse(
                        true,
                        "Signup successful",
                        "test@example.com",
                        "jwt-token"
                ));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService).signup(any(SignupRequest.class));
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception {

        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());
    }
}