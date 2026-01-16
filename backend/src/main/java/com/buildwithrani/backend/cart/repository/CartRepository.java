package com.buildwithrani.backend.cart.repository;

import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Each user has only one cart
    Optional<Cart> findByUser(User user);
}
