package com.buildwithrani.backend.cart.service;

import com.buildwithrani.backend.cart.dto.AddToCartRequest;
import com.buildwithrani.backend.cart.dto.CartResponse;
import com.buildwithrani.backend.cart.dto.UpdateCartItemRequest;
import com.buildwithrani.backend.cart.entity.Cart;

public interface CartService {

    CartResponse getCart();

    CartResponse addToCart(AddToCartRequest request);

    CartResponse updateCartItem(UpdateCartItemRequest request);

    void removeItem(Long productId);

    void clearCart();

    Cart getCurrentUserCart();

}
