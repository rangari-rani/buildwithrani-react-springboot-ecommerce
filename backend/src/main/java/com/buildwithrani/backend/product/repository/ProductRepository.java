package com.buildwithrani.backend.product.repository;

import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.common.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status);

    // Featured products (limit 5)
    List<Product> findTop5ByFeaturedTrueAndStatusOrderByCreatedAtDesc(
            ProductStatus status
    );

    // New arrivals (latest 5)
    List<Product> findTop5ByStatusOrderByCreatedAtDesc(
            ProductStatus status
    );

}
