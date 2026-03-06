package com.buildwithrani.backend.unit.cart;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.cart.dto.CartResponse;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.mapper.CartMapper;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class CartMapperTest {

    private final CartMapper cartMapper = new CartMapper();

    @Test
    void shouldMapCartToCartResponse() {

        // Arrange

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Cart cart = new Cart(user);

        Product product = Product.builder()
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("50000"))
                .discountPercentage(10)
                .imageUrl("image.jpg")
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .stock(10)
                .build();

        ReflectionTestUtils.setField(product, "id", 1L);

        // use domain method
        cart.addProduct(product, 2);

        // Act
        CartResponse response = cartMapper.toResponse(cart);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getItems().size());

        CartResponse.CartItemResponse item = response.getItems().get(0);

        assertEquals(1L, item.getProductId());
        assertEquals("Laptop", item.getName());
        assertEquals("image.jpg", item.getImageUrl());
        assertEquals(new BigDecimal("50000"), item.getPrice());
        assertEquals(2, item.getQuantity());
    }
}