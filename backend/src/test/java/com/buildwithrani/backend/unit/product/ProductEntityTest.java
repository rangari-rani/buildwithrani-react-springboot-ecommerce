package com.buildwithrani.backend.unit.product;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.product.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductEntityTest {

    @Test
    void shouldThrowWhenStockNegative() {
        assertThrows(InvalidStateException.class, () ->
                Product.builder()
                        .name("Laptop")
                        .description("Gaming")
                        .price(BigDecimal.valueOf(100))
                        .status(ProductStatus.ACTIVE)
                        .stock(-1)
                        .build()
        );
    }

    @Test
    void shouldThrowWhenDiscountInvalid() {

        Product product = buildProduct();

        assertThrows(InvalidStateException.class,
                () -> product.applyDiscount(95));

        assertThrows(InvalidStateException.class,
                () -> product.applyDiscount(-5));
    }

    @Test
    void shouldReduceStock() {

        Product product = buildProduct();

        product.reduceStock(2);

        assertEquals(8, product.getStock());
    }

    @Test
    void shouldThrowWhenReducingInvalidQuantity() {

        Product product = buildProduct();

        assertThrows(InvalidStateException.class,
                () -> product.reduceStock(0));
    }

    @Test
    void shouldIncreaseStock() {

        Product product = buildProduct();

        product.increaseStock(5);

        assertEquals(15, product.getStock());
    }

    @Test
    void shouldUpdateDetails() {

        Product product = buildProduct();

        product.updateDetails(
                "New",
                "Updated",
                BigDecimal.valueOf(200),
                10,
                true,
                "img"
        );

        assertEquals("New", product.getName());
        assertEquals(10, product.getDiscountPercentage());
    }

    private Product buildProduct() {
        return Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .stock(10)
                .build();
    }
}

