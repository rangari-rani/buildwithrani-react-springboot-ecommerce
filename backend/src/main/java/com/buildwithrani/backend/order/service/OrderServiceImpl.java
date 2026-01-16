package com.buildwithrani.backend.order.service;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.cart.entity.Cart;
import com.buildwithrani.backend.cart.entity.CartItem;
import com.buildwithrani.backend.cart.repository.CartItemRepository;
import com.buildwithrani.backend.cart.service.CartService;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.mapper.OrderMapper;
import com.buildwithrani.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.buildwithrani.backend.order.dto.OrderResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    // ================= USER METHODS =================

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

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> buildOrderItem(cartItem, order))
                .toList();

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByCart(cart);

        //  MAP TO DTO INSIDE TRANSACTION
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



    // ================= ADMIN METHODS =================

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }



    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);

        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toOrderResponse(savedOrder);
    }


    // ================= HELPER =================

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
