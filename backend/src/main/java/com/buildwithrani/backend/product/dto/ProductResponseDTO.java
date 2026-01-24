package com.buildwithrani.backend.product.dto;

import com.buildwithrani.backend.common.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer discountPercentage;

    private String imageUrl;

    private boolean featured;

    private ProductStatus status;

    private LocalDateTime createdAt;
}
