package com.buildwithrani.backend.product.service;

import com.buildwithrani.backend.common.cloudinary.CloudinaryService;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.product.dto.ProductRequestDTO;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.mapper.ProductMapper;
import com.buildwithrani.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductMapper productMapper;

    // -------- ADMIN --------
    @Override
    public ProductResponseDTO createProduct(
            ProductRequestDTO request,
            MultipartFile image
    ) {

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(image);
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .discountPercentage(request.getDiscountPercentage())
                .featured(Boolean.TRUE.equals(request.getFeatured()))
                .imageUrl(imageUrl)
                .build();

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }


    @Override
    public ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO request,
            MultipartFile image
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setFeatured(Boolean.TRUE.equals(request.getFeatured()));

        //  ONLY update image if a new one is provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            product.setImageUrl(imageUrl);
        }

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }


    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    // -------- USER --------
    @Override
    public List<ProductResponseDTO> getActiveProducts() {
        return productRepository
                .findByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.toResponse(product);
    }

    @Override
    public void updateProductStatus(Long productId, ProductStatus status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStatus(status);
        productRepository.save(product);
    }

    @Override
    public void updateFeaturedStatus(Long productId, boolean featured) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setFeatured(featured);
        productRepository.save(product);
    }
    @Override
    public List<ProductResponseDTO> getFeaturedProducts() {
        return productRepository
                .findTop5ByFeaturedTrueAndStatusOrderByCreatedAtDesc(
                        ProductStatus.ACTIVE
                )
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getNewArrivals() {
        return productRepository
                .findTop5ByStatusOrderByCreatedAtDesc(
                        ProductStatus.ACTIVE
                )
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

}
