package com.buildwithrani.backend.cart.entity;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One cart per user
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> items = new ArrayList<>();

    protected Cart() {
        // for JPA
    }

    public Cart(User user) {
        this.user = user;
    }

    public void addProduct(Product product, int quantity) {
        CartItem item = findItem(product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(this, product, quantity);
                    items.add(newItem);
                    return newItem;
                });

        if (item.getId() != null) {
            item.increaseQuantity(quantity);
        }
    }

    public void updateProductQuantity(Product product, int quantity) {
        CartItem item = findItem(product)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found in cart")
                );


        item.updateQuantity(quantity);
    }

    public void removeProduct(Product product) {
        findItem(product).ifPresent(items::remove);
    }

    public void clear() {
        items.clear();
    }

    private Optional<CartItem> findItem(Product product) {
        return items.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();
    }
}
