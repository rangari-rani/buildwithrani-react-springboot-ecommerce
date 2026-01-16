package com.buildwithrani.backend.order.repository;

import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // User orders (with items)
    @Query("""
        select distinct o from Order o
        left join fetch o.orderItems
        where o.user = :user
        order by o.createdAt desc
    """)
    List<Order> findByUserWithItems(@Param("user") User user);

    // Single order (with items)
    @Query("""
        select o from Order o
        left join fetch o.orderItems
        where o.id = :orderId
    """)
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    // Admin
    @Query("""
        select distinct o from Order o
        left join fetch o.orderItems
        order by o.createdAt desc
    """)
    List<Order> findAllWithItems();
}
