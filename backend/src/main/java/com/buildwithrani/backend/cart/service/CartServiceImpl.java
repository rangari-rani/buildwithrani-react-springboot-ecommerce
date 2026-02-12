package com.buildwithrani.backend.cart.service;

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
    private final AuditService auditService;

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
    public CartResponse addToCart(AddToCartRequest request) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );


        cart.addProduct(product, request.getQuantity());
        auditService.logAction(
                getCurrentUserId(),
                ActorRole.USER,
                "CART_ITEM_ADDED",
                "CART",
                cart.getId()
        );
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );


        cart.updateProductQuantity(product, request.getQuantity());
        auditService.logAction(
                getCurrentUserId(),
                ActorRole.USER,
                "CART_ITEM_UPDATED",
                "CART",
                cart.getId()
        );
        return cartMapper.toResponse(cart);
    }

    @Override
    public void removeItem(Long productId) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );


        cart.removeProduct(product);
        auditService.logAction(
                getCurrentUserId(),
                ActorRole.USER,
                "CART_ITEM_REMOVED",
                "CART",
                cart.getId()
        );
    }

    @Override
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.clear();
        auditService.logAction(
                getCurrentUserId(),
                ActorRole.USER,
                "CART_CLEARED",
                "CART",
                cart.getId()
        );
    }

    // ----------------- HELPERS -----------------

    private Cart getOrCreateCart() {
        User user = getCurrentUser();

        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

}
