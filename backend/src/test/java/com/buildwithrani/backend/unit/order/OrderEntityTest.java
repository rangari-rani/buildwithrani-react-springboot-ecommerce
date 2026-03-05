package com.buildwithrani.backend.unit.order;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

public class OrderEntityTest {

    @Test
    void shouldCreateOrderWithDefaultStates() {

        User user = mock(User.class);

        Order order = Order.create(
                user,
                BigDecimal.valueOf(500),
                new ArrayList<>()
        );

        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        assertEquals(PaymentStatus.PENDING, order.getPaymentStatus());
        assertEquals(BigDecimal.valueOf(500), order.getTotalAmount());
    }

    @Test
    void shouldMarkPaymentCreated() {

        Order order = buildCreatedOrder();

        order.markPaymentCreated("razor-id");

        assertEquals(PaymentStatus.CREATED, order.getPaymentStatus());
        assertEquals("razor-id", order.getRazorpayOrderId());
    }

    @Test
    void shouldNotChangeIfPaymentAlreadyCreated() {

        Order order = buildCreatedOrder();
        order.markPaymentCreated("first-id");

        order.markPaymentCreated("second-id");

        assertEquals("first-id", order.getRazorpayOrderId());
    }

    @Test
    void shouldThrowIfPaymentAlreadySuccess() {

        Order order = buildPaidOrder();

        assertThrows(InvalidStateException.class,
                () -> order.markPaymentCreated("id"));
    }

    @Test
    void shouldMarkPaymentSuccess() {

        Order order = buildCreatedOrder();
        order.markPaymentCreated("razor-id");

        order.markPaymentSuccess("pay-id", "sig");

        assertEquals(PaymentStatus.SUCCESS, order.getPaymentStatus());
        assertEquals(OrderStatus.PAID, order.getOrderStatus());
        assertEquals("pay-id", order.getRazorpayPaymentId());
    }

    @Test
    void shouldNotProcessDuplicateSuccess() {

        Order order = buildCreatedOrder();
        order.markPaymentCreated("razor-id");

        order.markPaymentSuccess("pay-id", "sig");
        order.markPaymentSuccess("pay-id", "sig");

        assertEquals(PaymentStatus.SUCCESS, order.getPaymentStatus());
    }

    @Test
    void shouldThrowIfCannotTransitionToPaid() {

        Order order = buildCreatedOrder();

        order.advanceByAdmin(OrderStatus.CANCELLED);

        assertThrows(InvalidStateException.class,
                () -> order.markPaymentSuccess("p", "s"));
    }

    @Test
    void shouldCancelIfCreated() {

        Order order = buildCreatedOrder();

        order.cancelByUser();

        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
    }

    @Test
    void shouldThrowIfCancelAfterPaid() {

        Order order = buildCreatedOrder();
        order.markPaymentCreated("r");
        order.markPaymentSuccess("p", "s");

        assertThrows(InvalidStateException.class,
                order::cancelByUser);
    }

    @Test
    void shouldAdvanceByAdmin() {

        Order order = buildCreatedOrder();

        order.advanceByAdmin(OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, order.getOrderStatus());
    }

    @Test
    void shouldThrowOnInvalidAdminTransition() {

        Order order = buildCreatedOrder();

        assertThrows(InvalidStateException.class,
                () -> order.advanceByAdmin(OrderStatus.DELIVERED));
    }

    private Order buildCreatedOrder() {
        User user = mock(User.class);
        return Order.create(user, BigDecimal.valueOf(100), new ArrayList<>());
    }

    private Order buildPaidOrder() {
        Order order = buildCreatedOrder();
        order.markPaymentCreated("r");
        order.markPaymentSuccess("p", "s");
        return order;
    }
}

