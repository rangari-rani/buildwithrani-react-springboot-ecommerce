package com.buildwithrani.backend.cart.mapper;

import com.buildwithrani.backend.cart.dto.CartResponse;
import com.buildwithrani.backend.cart.entity.Cart;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        return new CartResponse(
                cart.getItems()
                        .stream()
                        .map(item -> new CartResponse.CartItemResponse(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getProduct().getImageUrl(),
                                item.getProduct().getPrice(),
                                item.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
