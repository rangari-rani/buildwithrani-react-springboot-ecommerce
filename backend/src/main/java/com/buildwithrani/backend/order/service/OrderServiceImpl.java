package com.buildwithrani.backend.order.service;

import com.buildwithrani.backend.audit.entity.Audit;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.auth.security.SecurityUtils;
import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.service.AuditService;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.entity.CartItem;
import com.buildwithrani.backend.cart.repository.CartItemRepository;
import com.buildwithrani.backend.cart.service.CartService;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.AccessDeniedException;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.order.dto.OrderResponse;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.mapper.OrderMapper;
import com.buildwithrani.backend.order.repository.OrderRepository;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ProductRepository productRepository;

    /* =====================
       USER ACTIONS
       ===================== */

    @Override
    @Audit(action = "ORDER_PLACED", entityType = "ORDER")
    public OrderResponse placeOrder(String email) {

        User user = getUserByEmail(email);
        Cart cart = cartService.getCurrentUserCart();

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place order with empty cart");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Long productId = cartItem.getProduct().getId();
            int quantity = cartItem.getQuantity();

            int rowsUpdated = productRepository.decreaseStockAtomic(productId, quantity);

            if (rowsUpdated == 0) {
                throw new InvalidStateException("Item '" + cartItem.getProduct().getName() +
                        "' is unavailable or has insufficient stock.");
            }


            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .priceAtPurchase(product.getPrice())
                    .quantity(quantity)
                    .totalPrice(itemTotal)
                    .build();

            orderItems.add(orderItem);
        }

        Order order = Order.create(user, totalAmount, orderItems);
        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);

        cartService.clearCart();

        return OrderMapper.toOrderResponse(order);
    }

    /* =====================
     cancel order
     ===================== */
    @Override
    public OrderResponse cancelOrder(Long orderId, String email) {
        User user = getUserByEmail(email);

        // 1. Fetch the order with its items
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // 2. Security Check
        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }

        // 3. Update Order Status
        OrderStatus previousStatus = order.getOrderStatus();
        order.cancelByUser();

        // 4. ATOMIC STOCK RETURN
        // We loop through the items that were in the order and put them back on the shelf
        for (OrderItem item : order.getOrderItems()) {
            productRepository.increaseStockAtomic(item.getProductId(), item.getQuantity());
        }

        // 5. Professional logging
        String metadata = String.format("{\"from_status\":\"%s\", \"to_status\":\"CANCELLED\"}", previousStatus);

        auditService.logAction(
                user.getId(),
                ActorRole.USER,
                "ORDER_CANCELLED",
                "ORDER",
                order.getId(),
                metadata
        );

        return OrderMapper.toOrderResponse(order);
    }

    /* =====================
       ADMIN ACTIONS
       ===================== */

    @Override
    public OrderResponse advanceOrderStatus(Long orderId, OrderStatus nextStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus previousStatus = order.getOrderStatus();
        order.advanceByAdmin(nextStatus);

        String adminEmail = SecurityUtils.getCurrentUserEmail();
        Long adminId = (adminEmail != null) ?
                userRepository.findByEmail(adminEmail).map(User::getId).orElse(null) : null;

        // Structured metadata for easier auditing
        String metadata = String.format("{\"transition\":\"%s_TO_%s\"}", previousStatus, nextStatus);

        auditService.logAction(
                adminId,
                ActorRole.ADMIN,
                "ORDER_STATUS_CHANGED",
                "ORDER",
                order.getId(),
                metadata // 6th argument added
        );

        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {
        User user = getUserByEmail(email);
        return orderRepository.findByUserWithItems(user)
                .stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long orderId, String email) {
        User user = getUserByEmail(email);
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
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
                .totalPrice(priceAtPurchase.multiply(BigDecimal.valueOf(quantity)))
                .build();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}