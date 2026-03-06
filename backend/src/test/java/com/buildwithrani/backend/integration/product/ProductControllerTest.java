package com.buildwithrani.backend.integration.product;

import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnAllActiveProducts() throws Exception {

        when(productService.getActiveProducts())
                .thenReturn(List.of(new ProductResponseDTO()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());

        verify(productService).getActiveProducts();
    }

    @Test
    void shouldReturnProductById() throws Exception {

        when(productService.getProductById(1L))
                .thenReturn(new ProductResponseDTO());

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk());

        verify(productService).getProductById(1L);
    }

    @Test
    void shouldReturnFeaturedProducts() throws Exception {

        when(productService.getFeaturedProducts())
                .thenReturn(List.of(new ProductResponseDTO()));

        mockMvc.perform(get("/api/products/featured"))
                .andExpect(status().isOk());

        verify(productService).getFeaturedProducts();
    }

    @Test
    void shouldReturnNewArrivals() throws Exception {

        when(productService.getNewArrivals())
                .thenReturn(List.of(new ProductResponseDTO()));

        mockMvc.perform(get("/api/products/new-arrivals"))
                .andExpect(status().isOk());

        verify(productService).getNewArrivals();
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {

        when(productService.getProductById(1L))
                .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
