package com.buildwithrani.backend.product.mapper;

import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponseDTO toResponse(Product product) {
        if (product == null) return null;

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPercentage(product.getDiscountPercentage())
                .imageUrl(product.getImageUrl())
                .featured(product.isFeatured())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
