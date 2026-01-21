package com.buildwithrani.backend.product.controller;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.dto.ProductRequestDTO;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.buildwithrani.backend.common.dto.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    // -------- CREATE PRODUCT --------
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestPart("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "featured", defaultValue = "false") boolean featured
    ) {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name(name)
                .description(description)
                .price(price)
                .featured(featured)
                .build();

        ProductResponseDTO created = productService.createProduct(dto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // -------- UPDATE PRODUCT --------
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) Integer discountPercentage,
            @RequestParam boolean featured,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name(name)
                .description(description)
                .price(price)
                .discountPercentage(discountPercentage)
                .featured(featured)
                .build();

        ProductResponseDTO updated =
                productService.updateProduct(id, dto, image);

        return ResponseEntity.ok(updated);
    }

    // -------- GET ALL PRODUCTS (ADMIN) --------
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // -------- UPDATE PRODUCT STATUS --------
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status
    ) {
        productService.updateProductStatus(id, status);

        return ResponseEntity.ok(
                ApiResponse.success("Product status updated successfully")
        );
    }


    // -------- TOGGLE FEATURED --------
    @PatchMapping("/{id}/featured")
    public ResponseEntity<ApiResponse<Void>> updateFeaturedStatus(
            @PathVariable Long id,
            @RequestParam boolean featured
    ) {
        productService.updateFeaturedStatus(id, featured);

        return ResponseEntity.ok(
                ApiResponse.success("Product featured status updated successfully")
        );
    }

}
