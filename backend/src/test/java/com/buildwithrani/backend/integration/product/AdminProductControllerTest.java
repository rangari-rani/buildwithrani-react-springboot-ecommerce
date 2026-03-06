package com.buildwithrani.backend.integration.product;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "ADMIN")
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {

        MockMultipartFile image =
                new MockMultipartFile(
                        "image",
                        "test.jpg",
                        MediaType.IMAGE_JPEG_VALUE,
                        "image-content".getBytes()
                );

        when(productService.createProduct(any(), any()))
                .thenReturn(new ProductResponseDTO());

        mockMvc.perform(multipart("/api/admin/products")
                        .file(image)
                        .param("name", "Laptop")
                        .param("description", "Gaming laptop")
                        .param("price", "50000")
                        .param("discountPercentage", "10")
                        .param("featured", "true")
                        .param("stock", "20"))
                .andExpect(status().isCreated());

        verify(productService).createProduct(any(), any());
    }

    @Test
    void shouldUpdateProduct() throws Exception {

        MockMultipartFile image =
                new MockMultipartFile(
                        "image",
                        "test.jpg",
                        MediaType.IMAGE_JPEG_VALUE,
                        "image-content".getBytes()
                );

        when(productService.updateProduct(anyLong(), any(), any()))
                .thenReturn(new ProductResponseDTO());

        mockMvc.perform(multipart("/api/admin/products/{id}", 1L)
                        .file(image)
                        .param("name", "Laptop")
                        .param("description", "Updated laptop")
                        .param("price", "55000")
                        .param("discountPercentage", "5")
                        .param("featured", "true")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(productService).updateProduct(anyLong(), any(), any());
    }

    @Test
    void shouldIncreaseStock() throws Exception {

        mockMvc.perform(patch("/api/admin/products/{id}/stock/increase", 1L)
                        .param("quantity", "10"))
                .andExpect(status().isOk());

        verify(productService).increaseStock(1L, 10);
    }

    @Test
    void shouldDecreaseStock() throws Exception {

        mockMvc.perform(patch("/api/admin/products/{id}/stock/decrease", 1L)
                        .param("quantity", "5"))
                .andExpect(status().isOk());

        verify(productService).decreaseStock(1L, 5);
    }

    @Test
    void shouldGetAllProducts() throws Exception {

        when(productService.getAllProducts())
                .thenReturn(List.of(new ProductResponseDTO()));

        mockMvc.perform(get("/api/admin/products"))
                .andExpect(status().isOk());

        verify(productService).getAllProducts();
    }

    @Test
    void shouldUpdateProductStatus() throws Exception {

        mockMvc.perform(patch("/api/admin/products/{id}/status", 1L)
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk());

        verify(productService).updateProductStatus(1L, ProductStatus.ACTIVE);
    }

    @Test
    void shouldUpdateFeaturedStatus() throws Exception {

        mockMvc.perform(patch("/api/admin/products/{id}/featured", 1L)
                        .param("featured", "true"))
                .andExpect(status().isOk());

        verify(productService).updateFeaturedStatus(1L, true);
    }
}
