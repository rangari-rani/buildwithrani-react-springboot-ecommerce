package com.buildwithrani.backend.cart.service;

import com.buildwithrani.backend.cart.dto.AddToCartRequest;
import com.buildwithrani.backend.cart.dto.CartResponse;
import com.buildwithrani.backend.cart.dto.UpdateCartItemRequest;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.entity.CartItem;
import com.buildwithrani.backend.cart.mapper.CartMapper;
import com.buildwithrani.backend.cart.repository.CartRepository;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.repository.ProductRepository;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Cart cart;

    @BeforeEach
    void setup() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        user = new User();
        user.setEmail("user@test.com");

        cart = new Cart(user);
    }

    private Product buildActiveProduct(int stock) {
        return Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(1000))
                .discountPercentage(null)
                .imageUrl(null)
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .stock(stock)
                .build();
    }

    // ---------------- ADD TO CART ----------------

    @Test
    void shouldAddProductToCart() {

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        Product product = buildActiveProduct(10);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        CartResponse mockedResponse = mock(CartResponse.class);
        when(cartMapper.toResponse(cart)).thenReturn(mockedResponse);

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        CartResponse result = cartService.addToCart(request);

        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(cartMapper).toResponse(cart);
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart(request));
    }

    @Test
    void shouldThrowWhenProductInactive() {

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        Product product = buildActiveProduct(10);
        product.changeStatus(ProductStatus.INACTIVE);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        assertThrows(InvalidStateException.class,
                () -> cartService.addToCart(request));
    }

    @Test
    void shouldThrowWhenQuantityInvalid() {

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        Product product = buildActiveProduct(10);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(0);

        assertThrows(InvalidStateException.class,
                () -> cartService.addToCart(request));
    }

    @Test
    void shouldThrowWhenStockInsufficient() {

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        Product product = buildActiveProduct(1);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(5);

        assertThrows(InvalidStateException.class,
                () -> cartService.addToCart(request));
    }


    @Test
    void shouldUpdateCartItemSuccessfully() {

        User user = buildUser(1L);
        Cart cart = new Cart(user);

        Product product = buildActiveProduct(1L, 10);
        setId(product, 1L);

        CartItem item = new CartItem(cart, product, 2);
        cart.getItems().add(item);

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setProductId(1L);
        request.setQuantity(5);

        when(cartMapper.toResponse(cart)).thenReturn(mock(CartResponse.class));

        CartResponse response = cartService.updateCartItem(request);

        assertNotNull(response);
        verify(cartMapper).toResponse(cart);
    }

    @Test
    void shouldRemoveItemSuccessfully() {

        User user = buildUser(1L);
        Cart cart = new Cart(user);

        Product product = buildActiveProduct(1L, 10);
        setId(product, 1L);

        CartItem item = new CartItem(cart, product, 2);
        cart.getItems().add(item);


        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        cartService.removeItem(1L);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldClearCartSuccessfully() {

        User user = buildUser(1L);
        Cart cart = new Cart(user);

        Product product = buildActiveProduct(1L, 10);
        setId(product, 1L);

        cart.getItems().add(new CartItem(cart, product, 2));

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        cartService.clearCart();

        assertTrue(cart.getItems().isEmpty());
    }



    private User buildUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("user@test.com");
        return user;
    }

    private Product buildActiveProduct(Long id, int stock) {
        Product product = Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(100))
                .discountPercentage(null)
                .imageUrl(null)
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .stock(stock)
                .build();
        return product;
    }

    private void setId(Object entity, Long id) {
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}