package com.buildwithrani.backend.cart.entity;

import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many items belong to one cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Product added to cart
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Quantity of product
    @Column(nullable = false)
    private Integer quantity;

    protected CartItem() {
        // for JPA
    }

    public CartItem(Cart cart, Product product, int initialQuantity) {
        if (initialQuantity <= 0) {
            throw new InvalidStateException("Quantity must be greater than zero");
        }
        this.cart = cart;
        this.product = product;
        this.quantity = initialQuantity;
    }

    public void increaseQuantity(int delta) {
        if (delta <= 0) {
            throw new InvalidStateException("Quantity increment must be positive");
        }
        this.quantity += delta;
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new InvalidStateException("Quantity must be greater than zero");
        }
        this.quantity = newQuantity;
    }

}
