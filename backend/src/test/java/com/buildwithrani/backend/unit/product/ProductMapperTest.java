package com.buildwithrani.backend.unit.product;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductMapperTest {
    @Test
    void shouldMapProductToResponseDTO() {

        Product product = Product.builder()
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("50000"))
                .discountPercentage(10)
                .imageUrl("image.jpg")
                .featured(true)
                .status(ProductStatus.ACTIVE)
                .stock(20)
                .build();

        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(product, "createdAt", LocalDateTime.now());

        ProductMapper mapper = new ProductMapper();

        ProductResponseDTO response = mapper.toResponse(product);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
        assertEquals(product.getDiscountPercentage(), response.getDiscountPercentage());
        assertEquals(product.getImageUrl(), response.getImageUrl());
        assertEquals(product.isFeatured(), response.isFeatured());
        assertEquals(product.getStatus(), response.getStatus());
        assertEquals(product.getStock(), response.getStock());
        assertEquals(product.getCreatedAt(), response.getCreatedAt());
    }

    @Test
    void shouldReturnNullWhenProductIsNull() {

        ProductMapper mapper = new ProductMapper();

        ProductResponseDTO response = mapper.toResponse(null);

        assertNull(response);
    }

    @Test
    void shouldMapProductWhenOptionalFieldsAreNull() {

        Product product = Product.builder()
                .name("Phone")
                .description("Smartphone")
                .price(new BigDecimal("20000"))
                .discountPercentage(null)
                .imageUrl(null)
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .stock(15)
                .build();

        ReflectionTestUtils.setField(product, "id", 2L);

        ProductMapper mapper = new ProductMapper();

        ProductResponseDTO response = mapper.toResponse(product);

        assertNotNull(response);
        assertNull(response.getDiscountPercentage());
        assertNull(response.getImageUrl());
    }
}
