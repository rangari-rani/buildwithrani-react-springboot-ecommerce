package com.buildwithrani.backend.cart.entity;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.product.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartItemTest {

    private final User user = new User();
    private final Product product = Product.builder()
            .name("Laptop")
            .description("Gaming")
            .price(BigDecimal.valueOf(100))
            .status(ProductStatus.ACTIVE)
            .stock(10)
            .featured(false)
            .build();

    @Test
    void shouldCreateCartItemWithValidQuantity() {
        Cart cart = new Cart(user);
        CartItem item = new CartItem(cart, product, 2);

        assertEquals(2, item.getQuantity());
    }

    @Test
    void shouldThrowWhenCreatingWithZeroQuantity() {
        Cart cart = new Cart(user);

        assertThrows(InvalidStateException.class,
                () -> new CartItem(cart, product, 0));
    }

    @Test
    void shouldIncreaseQuantity() {
        Cart cart = new Cart(user);
        CartItem item = new CartItem(cart, product, 2);

        item.increaseQuantity(3);

        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowWhenIncreasingWithNegativeDelta() {
        Cart cart = new Cart(user);
        CartItem item = new CartItem(cart, product, 2);

        assertThrows(InvalidStateException.class,
                () -> item.increaseQuantity(0));
    }

    @Test
    void shouldUpdateQuantity() {
        Cart cart = new Cart(user);
        CartItem item = new CartItem(cart, product, 2);

        item.updateQuantity(5);

        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowWhenUpdatingToInvalidQuantity() {
        Cart cart = new Cart(user);
        CartItem item = new CartItem(cart, product, 2);

        assertThrows(InvalidStateException.class,
                () -> item.updateQuantity(0));
    }
}
