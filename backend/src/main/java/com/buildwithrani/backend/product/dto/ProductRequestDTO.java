package com.buildwithrani.backend.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    private String name;

    private String description;

    private BigDecimal price;

    private Integer discountPercentage;

    private Boolean featured;
}
