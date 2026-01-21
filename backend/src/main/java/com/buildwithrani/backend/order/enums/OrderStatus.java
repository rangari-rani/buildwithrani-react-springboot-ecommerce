package com.buildwithrani.backend.order.enums;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {

    CREATED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return EnumSet.of(PAID, CANCELLED);
        }
    },

    PAID {
        @Override
        public Set<OrderStatus> allowedNext() {
            return EnumSet.of(PACKED, CANCELLED);
        }
    },

    PACKED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return EnumSet.of(SHIPPED);
        }
    },

    SHIPPED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return EnumSet.of(DELIVERED);
        }
    },

    DELIVERED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return EnumSet.noneOf(OrderStatus.class);
        }
    },

    CANCELLED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return EnumSet.noneOf(OrderStatus.class);
        }
    };

    public abstract Set<OrderStatus> allowedNext();

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return allowedNext().contains(nextStatus);
    }

    public boolean isTerminal() {
        return this == DELIVERED || this == CANCELLED;
    }
}
