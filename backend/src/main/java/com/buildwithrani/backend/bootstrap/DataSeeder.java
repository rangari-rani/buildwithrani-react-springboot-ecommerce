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

        Product candle = new Product();
        candle.setName("Candle");
        candle.setDescription("Demo scented candle");
        candle.setPrice(new BigDecimal("300"));
        candle.setFeatured(true);
        candle.setStatus(ProductStatus.ACTIVE);
        candle.setImageUrl("https://res.cloudinary.com/dgqjia7ca/image/upload/v1768628114/products/lqogqmuv4hg2s7wfphqg.jpg");

        Product soap = new Product();
        soap.setName("Handmade Soap");
        soap.setDescription("Natural lavender soap");
        soap.setPrice(new BigDecimal("150"));
        soap.setFeatured(false);
        soap.setStatus(ProductStatus.ACTIVE);
        soap.setImageUrl("https://res.cloudinary.com/dgqjia7ca/image/upload/v1768301153/products/gbav4j4pi1dqm92put80.jpg");

        Product diffuser = new Product();
        diffuser.setName("Aroma Diffuser");
        diffuser.setDescription("Essential oil diffuser");
        diffuser.setPrice(new BigDecimal("1200"));
        diffuser.setFeatured(true);
        diffuser.setStatus(ProductStatus.ACTIVE);
        diffuser.setImageUrl("https://res.cloudinary.com/dgqjia7ca/image/upload/v1768301338/products/h3azzt1s8rse6dcdlw0p.jpg");

        productRepository.saveAll(List.of(candle, soap, diffuser));

        System.out.println("✅ Demo products created");
    }

    private void seedOrder() {

        User user = userRepository.findByEmail("user@demo.com").orElseThrow();
        Product candle = productRepository.findAll().get(0);

        OrderItem item = OrderItem.builder()
                .productId(candle.getId())
                .productName(candle.getName())
                .priceAtPurchase(candle.getPrice())
                .quantity(2)
                .totalPrice(candle.getPrice().multiply(BigDecimal.valueOf(2)))
                .build();

        BigDecimal total = item.getTotalPrice();

        Order order = Order.create(
                user,
                total,
                List.of(item)
        );

        // Attach order reference to item (important)
        item.setOrder(order);

        // Simulate payment
        order.markPaymentCreated("rzp_demo_order");
        order.markPaymentSuccess("rzp_demo_payment", "demo_signature");

        orderRepository.save(order);

        System.out.println("✅ Demo order created");
    }
}