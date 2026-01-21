package com.buildwithrani.backend.order.service;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.auth.security.SecurityUtils;
import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.service.AuditService;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.entity.CartItem;
import com.buildwithrani.backend.cart.repository.CartItemRepository;
import com.buildwithrani.backend.cart.service.CartService;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.mapper.OrderMapper;
import com.buildwithrani.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final AuditService auditService;

    /* =====================
       USER ACTIONS
       ===================== */

    @Override
    public OrderResponse placeOrder(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal totalAmount = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> buildOrderItem(cartItem, null)) // temporary
                .toList();

        Order order = Order.create(user, totalAmount, orderItems);

        // fix back-reference
        orderItems.forEach(item -> item.setOrder(order));

        // Mock payment success → explicit transition
        order.markAsPaid();

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByCart(cart);
        cart.getItems().clear();

        return OrderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserWithItems(user)
                .stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long orderId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        OrderStatus previousStatus = order.getOrderStatus();

        order.cancelByUser();

        auditService.logAction(
                user.getId(),
                ActorRole.USER,
                "ORDER_CANCELLED_FROM_" + previousStatus,
                "ORDER",
                order.getId()
        );

        return OrderMapper.toOrderResponse(order);
    }

    /* =====================
       ADMIN ACTIONS
       ===================== */

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse advanceOrderStatus(
            Long orderId,
            OrderStatus nextStatus
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus previousStatus = order.getOrderStatus();

        order.advanceByAdmin(nextStatus);

        String adminEmail = SecurityUtils.getCurrentUserEmail();

        Long adminId = null;
        if (adminEmail != null) {
            adminId = userRepository.findByEmail(adminEmail)
                    .map(User::getId)
                    .orElse(null);
        }

        auditService.logAction(
                adminId,
                ActorRole.ADMIN,
                "ORDER_STATUS_CHANGED_" + previousStatus + "_TO_" + nextStatus,
                "ORDER",
                order.getId()
        );

        return OrderMapper.toOrderResponse(order);
    }

    /* =====================
       HELPERS
       ===================== */

    private OrderItem buildOrderItem(CartItem cartItem, Order order) {

        BigDecimal priceAtPurchase = cartItem.getProduct().getPrice();
        Integer quantity = cartItem.getQuantity();

        return OrderItem.builder()
                .order(order)
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .priceAtPurchase(priceAtPurchase)
                .quantity(quantity)
                .totalPrice(
                        priceAtPurchase.multiply(BigDecimal.valueOf(quantity))
                )
                .build();
    }
}
