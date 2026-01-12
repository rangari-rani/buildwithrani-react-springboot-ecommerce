package com.buildwithrani.backend.product.service;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.dto.ProductRequestDTO;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    // -------- ADMIN --------
    ProductResponseDTO createProduct(ProductRequestDTO request);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO request);
    List<ProductResponseDTO> getAllProducts();

    // -------- USER --------
    List<ProductResponseDTO> getActiveProducts();

    ProductResponseDTO getProductById(Long productId);

    void updateProductStatus(Long productId, ProductStatus status);

    void updateFeaturedStatus(Long productId, boolean isFeatured);

}
