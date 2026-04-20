package com.buildwithrani.backend.product.repository;

import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.common.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status);
    List<Product> findTop5ByFeaturedTrueAndStatusOrderByCreatedAtDesc(ProductStatus status);
    List<Product> findTop5ByStatusOrderByCreatedAtDesc(ProductStatus status);

    /**
     * ATOMIC DECREASE: This is the Double-Buy Protection.
     * It only updates if stock is sufficient.
     * Returns 1 if successful, 0 if someone else took the last item.
     */
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity " +
            "WHERE p.id = :id AND p.stock >= :quantity AND p.status = 'ACTIVE'")
    int decreaseStockAtomic(@Param("id") Long id, @Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock + :quantity WHERE p.id = :id")
    void increaseStockAtomic(@Param("id") Long id, @Param("quantity") int quantity);
}
