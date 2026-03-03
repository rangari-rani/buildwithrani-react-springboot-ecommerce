package com.buildwithrani.backend.order.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderStatusTest {

    @Test
    void shouldAllowValidTransitions() {
        assertTrue(OrderStatus.CREATED.canTransitionTo(OrderStatus.PAID));
        assertTrue(OrderStatus.CREATED.canTransitionTo(OrderStatus.CANCELLED));
    }

    @Test
    void shouldNotAllowInvalidTransition() {
        assertFalse(OrderStatus.CREATED.canTransitionTo(OrderStatus.SHIPPED));
    }

    @Test
    void shouldDetectTerminalStates() {
        assertTrue(OrderStatus.DELIVERED.isTerminal());
        assertTrue(OrderStatus.CANCELLED.isTerminal());
        assertFalse(OrderStatus.PAID.isTerminal());
    }
}

