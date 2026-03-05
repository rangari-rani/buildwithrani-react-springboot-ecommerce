package com.buildwithrani.backend.unit.cart;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.product.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartEntityTest {

    private User user = new User();

    private Product buildProduct() {
        return Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .stock(10)
                .featured(false)
                .build();
    }

    @Test
    void shouldAddNewProductToCart() {
        Cart cart = new Cart(user);
        Product product = buildProduct();

        cart.addProduct(product, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void shouldIncreaseQuantityIfProductAlreadyExists() {
        Cart cart = new Cart(user);
        Product product = buildProduct();

        cart.addProduct(product, 2);
        cart.addProduct(product, 3);

        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getQuantity());
    }

    @Test
    void shouldUpdateProductQuantity() {
        Cart cart = new Cart(user);
        Product product = buildProduct();

        cart.addProduct(product, 2);
        cart.updateProductQuantity(product, 5);

        assertEquals(5, cart.getItems().get(0).getQuantity());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingProduct() {
        Cart cart = new Cart(user);
        Product product = buildProduct();

        assertThrows(ResourceNotFoundException.class,
                () -> cart.updateProductQuantity(product, 3));
    }

    @Test
    void shouldRemoveProduct() {
        Cart cart = new Cart(user);
        Product product = buildProduct();

        cart.addProduct(product, 2);
        cart.removeProduct(product);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldClearCart() {
        Cart cart = new Cart(user);
        Product product = buildProduct();

        cart.addProduct(product, 2);
        cart.clear();

        assertTrue(cart.getItems().isEmpty());
    }
}
