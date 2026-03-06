package com.buildwithrani.backend.unit;

import com.buildwithrani.backend.common.dto.ErrorResponse;
import com.buildwithrani.backend.common.exception.AccessDeniedException;
import com.buildwithrani.backend.common.exception.GlobalExceptionHandler;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void shouldHandleResourceNotFoundException() {

        when(request.getRequestURI()).thenReturn("/api/products/1");

        ResourceNotFoundException ex =
                new ResourceNotFoundException("Product not found");

        ResponseEntity<ErrorResponse> response =
                handler.handleNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getErrorCode());
        assertEquals("Product not found", response.getBody().getMessage());
    }

    @Test
    void shouldHandleInvalidStateException() {

        when(request.getRequestURI()).thenReturn("/api/cart");

        InvalidStateException ex =
                new InvalidStateException("Invalid cart state");

        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidState(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("INVALID_STATE", response.getBody().getErrorCode());
    }
    @Test
    void shouldHandleAccessDeniedException() {

        when(request.getRequestURI()).thenReturn("/api/admin");

        AccessDeniedException ex =
                new AccessDeniedException("Forbidden");

        ResponseEntity<ErrorResponse> response =
                handler.handleAccessDenied(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("ACCESS_DENIED", response.getBody().getErrorCode());
    }

    @Test
    void shouldHandleValidationException() {

        when(request.getRequestURI()).thenReturn("/api/products");

        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError =
                new FieldError("object", "name", "must not be blank");

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response =
                handler.handleValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void shouldHandleOptimisticLockException() {

        when(request.getRequestURI()).thenReturn("/api/order");

        OptimisticLockException ex =
                new OptimisticLockException();

        ResponseEntity<ErrorResponse> response =
                handler.handleOptimisticLock(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("CONCURRENT_MODIFICATION", response.getBody().getErrorCode());
    }

    @Test
    void shouldHandleDataIntegrityViolation() {

        when(request.getRequestURI()).thenReturn("/api/products");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("constraint");

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrity(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldHandleGenericException() {

        when(request.getRequestURI()).thenReturn("/api/test");

        Exception ex = new RuntimeException("unexpected");

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().getErrorCode());
    }
}
