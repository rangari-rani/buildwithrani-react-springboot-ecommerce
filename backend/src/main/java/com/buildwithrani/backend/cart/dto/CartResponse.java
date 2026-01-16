package com.buildwithrani.backend.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class CartResponse {

    private List<CartItemResponse> items;

    @Getter
    @AllArgsConstructor
    public static class CartItemResponse {
        private Long productId;
        private String name;
        private String imageUrl;
        private BigDecimal price;
        private Integer quantity;
    }
}
