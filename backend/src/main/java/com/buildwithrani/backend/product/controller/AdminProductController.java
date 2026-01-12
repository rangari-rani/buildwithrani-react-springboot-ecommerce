package com.buildwithrani.backend.product.controller;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.dto.ProductRequestDTO;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    // -------- CREATE PRODUCT --------
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody ProductRequestDTO request
    ) {
        ProductResponseDTO created =
                productService.createProduct(request);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // -------- UPDATE PRODUCT --------
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO request
    ) {
        ProductResponseDTO updated =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(updated);
    }
    // -------- GET ALL PRODUCTS (ADMIN) --------
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // -------- UPDATE PRODUCT STATUS --------
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status
    ) {
        productService.updateProductStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    // -------- TOGGLE FEATURED --------
    @PatchMapping("/{id}/featured")
    public ResponseEntity<Void> updateFeaturedStatus(
            @PathVariable Long id,
            @RequestParam boolean featured
    ) {
        productService.updateFeaturedStatus(id, featured);
        return ResponseEntity.noContent().build();
    }
}
