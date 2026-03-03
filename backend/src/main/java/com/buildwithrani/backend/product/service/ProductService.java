package com.buildwithrani.backend.product.service;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.dto.ProductRequestDTO;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // -------- ADMIN --------
    ProductResponseDTO createProduct(ProductRequestDTO request, MultipartFile image);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO request, MultipartFile image);

    void updateProductStatus(Long productId, ProductStatus status);

    void updateFeaturedStatus(Long productId, boolean isFeatured);

    // -------- INVENTORY --------
    void increaseStock(Long productId, int quantity);

    void decreaseStock(Long productId, int quantity);

    // -------- USER --------
    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> getActiveProducts();

    ProductResponseDTO getProductById(Long productId);

    List<ProductResponseDTO> getFeaturedProducts();

    List<ProductResponseDTO> getNewArrivals();
}
