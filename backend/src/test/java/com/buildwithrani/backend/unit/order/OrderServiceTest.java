package com.buildwithrani.backend.unit.order;

import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.service.AuditService;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.entity.CartItem;
import com.buildwithrani.backend.cart.service.CartService;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.AccessDeniedException;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.repository.OrderRepository;
import com.buildwithrani.backend.order.service.OrderServiceImpl;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.repository.ProductRepository;
import com.buildwithrani.backend.auth.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CartService cartService;
    @Mock private UserRepository userRepository;
    @Mock private AuditService auditService;
    @Mock private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User buildUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Product buildActiveProduct(Long id, BigDecimal price) {
        return Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(price)
                .discountPercentage(null)
                .imageUrl(null)
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .stock(10)
                .build();
    }

    // =====================
    // PLACE ORDER SUCCESS
    // =====================

    @Test
    void shouldPlaceOrderSuccessfully() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User user = buildUser(1L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(user));

            Cart cart = new Cart(user);

            Product product = buildActiveProduct(1L, BigDecimal.valueOf(100));
            setId(product, 1L);

            CartItem cartItem = new CartItem(cart, product, 2);
            cart.getItems().add(cartItem);

            when(cartService.getCurrentUserCart())
                    .thenReturn(cart);

            when(productRepository.findById(1L))
                    .thenReturn(Optional.of(product));

            OrderResponse response =
                    orderService.placeOrder("user@test.com");

            assertNotNull(response);

            verify(orderRepository).save(any(Order.class));
            verify(cartService).clearCart();
        }
    }

    // =====================
    // EMPTY CART
    // =====================

    @Test
    void shouldThrowWhenCartIsEmpty() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User user = buildUser(1L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(user));

            Cart emptyCart = new Cart(user);

            when(cartService.getCurrentUserCart())
                    .thenReturn(emptyCart);

            assertThrows(IllegalStateException.class,
                    () -> orderService.placeOrder("user@test.com"));
        }
    }

    // =====================
    // PRODUCT NOT ACTIVE
    // =====================

    @Test
    void shouldThrowWhenProductInactive() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User user = buildUser(1L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(user));

            Cart cart = new Cart(user);

            Product product = buildActiveProduct(1L, BigDecimal.valueOf(100));
            setId(product, 1L);
            product.changeStatus(ProductStatus.INACTIVE);

            CartItem cartItem = new CartItem(cart, product, 2);
            cart.getItems().add(cartItem);

            when(cartService.getCurrentUserCart())
                    .thenReturn(cart);

            when(productRepository.findById(1L))
                    .thenReturn(Optional.of(product));

            assertThrows(InvalidStateException.class,
                    () -> orderService.placeOrder("user@test.com"));
        }
    }

    @Test
    void shouldCancelOrderSuccessfully() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User user = buildUser(1L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(user));

            Order order = mock(Order.class);
            when(order.getUser()).thenReturn(user);
            when(order.getId()).thenReturn(10L);
            when(order.getOrderStatus()).thenReturn(OrderStatus.CREATED);

            when(orderRepository.findById(10L))
                    .thenReturn(Optional.of(order));

            OrderResponse response =
                    orderService.cancelOrder(10L, "user@test.com");

            assertNotNull(response);

            verify(order).cancelByUser();
            verify(auditService).logAction(
                    eq(1L),
                    eq(ActorRole.USER),
                    eq("ORDER_CANCELLED"),
                    eq("ORDER"),
                    eq(10L),
                    any()
            );
        }
    }

    @Test
    void shouldThrowWhenCancellingOtherUsersOrder() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User currentUser = buildUser(1L);
            User otherUser = buildUser(2L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(currentUser));

            Order order = mock(Order.class);
            when(order.getUser()).thenReturn(otherUser);

            when(orderRepository.findById(10L))
                    .thenReturn(Optional.of(order));

            assertThrows(AccessDeniedException.class,
                    () -> orderService.cancelOrder(10L, "user@test.com"));
        }
    }

    @Test
    void shouldAdvanceOrderStatusSuccessfully() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("admin@test.com");

            User admin = buildUser(99L);

            when(userRepository.findByEmail("admin@test.com"))
                    .thenReturn(Optional.of(admin));

            Order order = mock(Order.class);

            when(order.getOrderStatus()).thenReturn(OrderStatus.CREATED);
            when(order.getId()).thenReturn(10L);

            when(orderRepository.findById(10L))
                    .thenReturn(Optional.of(order));

            OrderResponse response =
                    orderService.advanceOrderStatus(
                            10L,
                            OrderStatus.PAID   // VALID transition
                    );

            assertNotNull(response);

            verify(order).advanceByAdmin(OrderStatus.PAID);

            verify(auditService).logAction(
                    eq(99L),
                    eq(ActorRole.ADMIN),
                    eq("ORDER_STATUS_CHANGED"),
                    eq("ORDER"),
                    eq(10L),
                    any()
            );
        }
    }

    @Test
    void shouldThrowWhenInvalidStatusTransition() {

        User user = buildUser(1L);

        Order order = Order.create(
                user,
                BigDecimal.valueOf(100),
                new ArrayList<>()
        );

        setId(order, 10L);

        // Order starts at CREATED (default in your factory)

        when(orderRepository.findById(10L))
                .thenReturn(Optional.of(order));

        assertThrows(InvalidStateException.class,
                () -> orderService.advanceOrderStatus(
                        10L,
                        OrderStatus.SHIPPED // invalid jump from CREATED
                ));
    }

    @Test
    void shouldThrowWhenUserNotFoundWhilePlacingOrder() {

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.placeOrder("user@test.com"));
    }

    @Test
    void shouldThrowWhenProductNotFound() {

        User user = buildUser(1L);

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        Cart cart = new Cart(user);

        Product product = buildActiveProduct(1L, BigDecimal.valueOf(100));
        setId(product, 1L);

        cart.getItems().add(new CartItem(cart, product, 2));

        when(cartService.getCurrentUserCart())
                .thenReturn(cart);

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.placeOrder("user@test.com"));
    }

    @Test
    void shouldThrowWhenStockInsufficient() {

        User user = buildUser(1L);

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        Cart cart = new Cart(user);

        Product product = Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(100))
                .discountPercentage(null)
                .imageUrl(null)
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .stock(1)
                .build();

        setId(product, 1L);

        cart.getItems().add(new CartItem(cart, product, 5));

        when(cartService.getCurrentUserCart())
                .thenReturn(cart);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(InvalidStateException.class,
                () -> orderService.placeOrder("user@test.com"));
    }

    @Test
    void shouldThrowWhenCancellingNonExistingOrder() {

        User user = buildUser(1L);

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.cancelOrder(99L, "user@test.com"));
    }

    @Test
    void shouldReturnMyOrders() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User user = buildUser(1L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(user));

            Order order = Order.create(
                    user,
                    BigDecimal.valueOf(100),
                    new ArrayList<>()
            );

            when(orderRepository.findByUserWithItems(user))
                    .thenReturn(List.of(order));

            List<OrderResponse> responses =
                    orderService.getMyOrders("user@test.com");

            assertEquals(1, responses.size());
        }
    }

    @Test
    void shouldReturnOrderById() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User user = buildUser(1L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(user));

            Order order = Order.create(
                    user,
                    BigDecimal.valueOf(100),
                    new ArrayList<>()
            );

            setId(order, 10L);

            when(orderRepository.findByIdWithItems(10L))
                    .thenReturn(Optional.of(order));

            OrderResponse response =
                    orderService.getOrderById(10L, "user@test.com");

            assertNotNull(response);
        }
    }

    @Test
    void shouldThrowWhenAccessingOtherUsersOrder() {

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("user@test.com");

            User currentUser = buildUser(1L);
            User otherUser = buildUser(2L);

            when(userRepository.findByEmail("user@test.com"))
                    .thenReturn(Optional.of(currentUser));

            Order order = Order.create(
                    otherUser,
                    BigDecimal.valueOf(100),
                    new ArrayList<>()
            );

            setId(order, 10L);

            when(orderRepository.findByIdWithItems(10L))
                    .thenReturn(Optional.of(order));

            assertThrows(AccessDeniedException.class,
                    () -> orderService.getOrderById(10L, "user@test.com"));
        }
    }

    @Test
    void shouldReturnAllOrders() {

        User user = buildUser(1L);

        Order order = Order.create(
                user,
                BigDecimal.valueOf(100),
                new ArrayList<>()
        );

        when(orderRepository.findAllWithItems())
                .thenReturn(List.of(order));

        List<OrderResponse> responses =
                orderService.getAllOrders();

        assertEquals(1, responses.size());
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