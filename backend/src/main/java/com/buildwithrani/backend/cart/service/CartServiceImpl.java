package com.buildwithrani.backend.cart.service;

import com.buildwithrani.backend.audit.entity.Audit;
import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.service.AuditService;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.cart.dto.AddToCartRequest;
import com.buildwithrani.backend.cart.dto.CartResponse;
import com.buildwithrani.backend.cart.dto.UpdateCartItemRequest;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.mapper.CartMapper;
import com.buildwithrani.backend.cart.repository.CartRepository;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    // REMOVED: auditService dependency is no longer needed here!

    @Override
    public CartResponse getCart() {
        Cart cart = getOrCreateCart();
        return cartMapper.toResponse(cart);
    }

    @Override
    public Cart getCurrentUserCart() {
        return getOrCreateCart();
    }

    @Override
    @Audit(action = "CART_ITEM_ADDED", entityType = "CART") // The Aspect handles the logging
    public CartResponse addToCart(AddToCartRequest request) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new InvalidStateException("Product is not available for purchase");
        }

        cart.addProduct(product, request.getQuantity());
        return cartMapper.toResponse(cart);
    }

    @Override
    @Audit(action = "CART_ITEM_UPDATED", entityType = "CART")
    public CartResponse updateCartItem(UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        cart.updateProductQuantity(product, request.getQuantity());
        return cartMapper.toResponse(cart);
    }

    @Override
    @Audit(action = "CART_ITEM_REMOVED", entityType = "CART")
    public void removeItem(Long productId) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        cart.removeProduct(product);
    }

    @Override
    @Audit(action = "CART_CLEARED", entityType = "CART")
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.clear();
    }

    // ----------------- HELPERS (Kept the same) -----------------
    private Cart getOrCreateCart() {
        User user = getCurrentUser();
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}