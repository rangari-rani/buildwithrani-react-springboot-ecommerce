package com.buildwithrani.backend.bootstrap;

import com.buildwithrani.backend.auth.model.Role;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;
import com.buildwithrani.backend.order.enums.OrderStatus;
import com.buildwithrani.backend.order.enums.PaymentStatus;
import com.buildwithrani.backend.order.repository.OrderRepository;
import com.buildwithrani.backend.product.entity.Product;

import com.buildwithrani.backend.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      ProductRepository productRepository,
                      OrderRepository orderRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {
            seedUsers();
        }

        if (productRepository.count() == 0) {
            seedProducts();
        }

        if (orderRepository.count() == 0) {
            seedOrder();
        }
    }

    private void seedUsers() {

        User admin = new User();
        admin.setFullName("Demo Admin");
        admin.setEmail("admin@demo.com");
        admin.setPassword(passwordEncoder.encode("Password123!"));
        admin.setRole(Role.ADMIN);

        User user = new User();
        user.setFullName("Demo User");
        user.setEmail("user@demo.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setRole(Role.USER);

        userRepository.saveAll(List.of(admin, user));

        System.out.println("✅ Demo users created");
    }

    private void seedProducts() {

        Product candle = Product.builder()
                .name("Candle")
                .description("Demo scented candle")
                .price(new BigDecimal("300"))
                .featured(true)
                .status(ProductStatus.ACTIVE)
                .imageUrl("https://res.cloudinary.com/dgqjia7ca/image/upload/v1768628114/products/lqogqmuv4hg2s7wfphqg.jpg")
                .stock(50)
                .build();

        Product soap = Product.builder()
                .name("Handmade Soap")
                .description("Natural lavender soap")
                .price(new BigDecimal("150"))
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .imageUrl("https://res.cloudinary.com/dgqjia7ca/image/upload/v1768301153/products/gbav4j4pi1dqm92put80.jpg")
                .stock(100)
                .build();

        Product diffuser = Product.builder()
                .name("Aroma Diffuser")
                .description("Essential oil diffuser")
                .price(new BigDecimal("1200"))
                .featured(true)
                .status(ProductStatus.ACTIVE)
                .imageUrl("https://res.cloudinary.com/dgqjia7ca/image/upload/v1768301338/products/h3azzt1s8rse6dcdlw0p.jpg")
                .stock(25)
                .build();

        productRepository.saveAll(List.of(candle, soap, diffuser));

        System.out.println("✅ Demo products created");
    }

    private void seedOrder() {

        User user = userRepository.findByEmail("user@demo.com").orElseThrow();
        Product candle = productRepository.findAll().get(0);

        // CREATED ORDER
        OrderItem createdItem = OrderItem.builder()
                .productId(candle.getId())
                .productName(candle.getName())
                .priceAtPurchase(candle.getPrice())
                .quantity(1)
                .totalPrice(candle.getPrice())
                .build();

        Order createdOrder = Order.create(
                user,
                createdItem.getTotalPrice(),
                List.of(createdItem)
        );

        createdItem.setOrder(createdOrder);

        orderRepository.save(createdOrder);

        // PAID ORDER
        candle.reduceStock(2); // reflect inventory change

        OrderItem paidItem = OrderItem.builder()
                .productId(candle.getId())
                .productName(candle.getName())
                .priceAtPurchase(candle.getPrice())
                .quantity(2)
                .totalPrice(candle.getPrice().multiply(BigDecimal.valueOf(2)))
                .build();

        Order paidOrder = Order.create(
                user,
                paidItem.getTotalPrice(),
                List.of(paidItem)
        );

        paidItem.setOrder(paidOrder);

        paidOrder.markPaymentCreated("rzp_demo_order");
        paidOrder.markPaymentSuccess("rzp_demo_payment", "demo_signature");

        orderRepository.save(paidOrder);

        System.out.println("✅ Demo order created");
    }
}