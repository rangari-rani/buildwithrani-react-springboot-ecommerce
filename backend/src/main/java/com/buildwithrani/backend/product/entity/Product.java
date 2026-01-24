package com.buildwithrani.backend.product.entity;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
