package com.buildwithrani.backend.product.service;

import com.buildwithrani.backend.common.cloudinary.CloudinaryService;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.product.dto.ProductRequestDTO;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.mapper.ProductMapper;
import com.buildwithrani.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.service.AuditService;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.auth.security.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductMapper productMapper;
    private final AuditService auditService;
    private final UserRepository userRepository;

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
                .featured(Boolean.TRUE.equals(request.getFeatured()))
                .status(ProductStatus.INACTIVE)
                .imageUrl(imageUrl)
                .build();

        product.applyDiscount(request.getDiscountPercentage());

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
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStatus() == ProductStatus.DISCONTINUED) {
            throw new InvalidStateException("Discontinued products cannot be modified");
        }
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.applyDiscount(request.getDiscountPercentage());
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
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.toResponse(product);
    }

    @Override
    public void updateProductStatus(Long productId, ProductStatus status) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStatus() == ProductStatus.DISCONTINUED) {
            throw new InvalidStateException("Discontinued products cannot be modified");
        }
        ProductStatus previousStatus = product.getStatus();

        // no-op protection (optional but clean)
        if (previousStatus == status) {
            return;
        }

        product.setStatus(status);
        productRepository.save(product);

        // ---- AUDIT LOGGING ----
        String action;

        if (previousStatus == ProductStatus.INACTIVE && status == ProductStatus.ACTIVE) {
            action = "PRODUCT_ACTIVATED";
        } else if (previousStatus == ProductStatus.ACTIVE && status == ProductStatus.INACTIVE) {
            action = "PRODUCT_DEACTIVATED";
        } else {
            action = "PRODUCT_STATUS_CHANGED_" + previousStatus + "_TO_" + status;
        }

        String adminEmail = SecurityUtils.getCurrentUserEmail();

        Long adminId = null;
        if (adminEmail != null) {
            adminId = userRepository.findByEmail(adminEmail)
                    .map(User::getId)
                    .orElse(null);
        }

        auditService.logAction(
                adminId,
                ActorRole.ADMIN,
                action,
                "PRODUCT",
                product.getId()
        );

    }


    @Override
    public void updateFeaturedStatus(Long productId, boolean featured) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStatus() == ProductStatus.DISCONTINUED) {
            throw new InvalidStateException("Discontinued products cannot be modified");
        }
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
