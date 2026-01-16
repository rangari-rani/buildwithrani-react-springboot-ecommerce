package com.buildwithrani.backend.cart.repository;

import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.entity.CartItem;
import com.buildwithrani.backend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find a specific product inside a user's cart
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteByCart(Cart cart);

}
