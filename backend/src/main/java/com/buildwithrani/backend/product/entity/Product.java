package com.buildwithrani.backend.product.entity;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_product_status", columnList = "status")
        }
)
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer discountPercentage;

    private String imageUrl;

    @Column(nullable = false)
    private boolean featured;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer stock;

    @Version
    @Column(nullable = false)
    private Long version;

    @Builder
    private Product(
            String name,
            String description,
            BigDecimal price,
            Integer discountPercentage,
            String imageUrl,
            boolean featured,
            ProductStatus status,
            Integer stock
    ) {
        if (stock == null || stock < 0) {
            throw new InvalidStateException("Stock must be non-negative");
        }

        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.status = status;
        this.stock = stock;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void applyDiscount(Integer discountPercentage) {
        if (discountPercentage != null &&
                (discountPercentage < 0 || discountPercentage > 90)) {
            throw new InvalidStateException("Invalid discount percentage");
        }
        this.discountPercentage = discountPercentage;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidStateException("Quantity must be positive");
        }

        if (this.stock < quantity) {
            throw new InvalidStateException("Insufficient stock");
        }

        this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidStateException("Quantity must be positive");
        }

        this.stock += quantity;
    }

    public void updateDetails(
            String name,
            String description,
            BigDecimal price,
            Integer discountPercentage,
            boolean featured,
            String imageUrl
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.featured = featured;
        this.imageUrl = imageUrl;
        applyDiscount(discountPercentage);
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }

    public void updateFeatured(boolean featured) {
        this.featured = featured;
    }
}