package com.buildwithrani.backend.order.entity;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.buildwithrani.backend.common.exception.InvalidStateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order belongs to a user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* =====================
       LIFECYCLE MANAGEMENT
       ===================== */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    /* =====================
       DOMAIN METHODS
       ===================== */

    public void markAsPaid() {
        if (!orderStatus.canTransitionTo(OrderStatus.PAID)) {
            throw new InvalidStateException(
                    "Cannot mark order as PAID from state: " + orderStatus
            );

        }
        this.paymentStatus = PaymentStatus.PAID;
        this.orderStatus = OrderStatus.PAID;
    }

    public void advanceByAdmin(OrderStatus nextStatus) {
        if (!orderStatus.canTransitionTo(nextStatus)) {
            throw new InvalidStateException(
                    "Invalid order transition from " + orderStatus + " to " + nextStatus
            );
        }
        this.orderStatus = nextStatus;
    }

    public void cancelByUser() {
        if (!(orderStatus == OrderStatus.CREATED || orderStatus == OrderStatus.PAID)) {
            throw new InvalidStateException(
                    "Order cannot be cancelled in state: " + orderStatus
            );
        }
        this.orderStatus = OrderStatus.CANCELLED;
    }
    public static Order create(
            User user,
            BigDecimal totalAmount,
            List<OrderItem> orderItems
    ) {
        Order order = new Order();
        order.user = user;
        order.totalAmount = totalAmount;
        order.orderItems = orderItems;


        order.orderStatus = OrderStatus.CREATED;
        order.paymentStatus = PaymentStatus.PENDING;

        return order;
    }
}
