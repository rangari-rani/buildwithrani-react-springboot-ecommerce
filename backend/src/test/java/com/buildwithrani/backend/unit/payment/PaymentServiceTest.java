package com.buildwithrani.backend.unit.payment;

import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.order.entity.Order;
import com.buildwithrani.backend.order.entity.OrderItem;
import com.buildwithrani.backend.order.enums.PaymentStatus;
import com.buildwithrani.backend.order.repository.OrderRepository;
import com.buildwithrani.backend.payment.gateway.PaymentGateway;
import com.buildwithrani.backend.payment.service.PaymentServiceImpl;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private PaymentGateway paymentGateway;
    @Mock private ProductRepository productRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order buildOrder(PaymentStatus status) {
        Order order = mock(Order.class);
        when(order.getPaymentStatus()).thenReturn(status);
        return order;
    }

    @Test
    void shouldCreatePaymentSuccessfully() {

        Order order = buildOrder(PaymentStatus.PENDING);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(paymentGateway.createOrder(BigDecimal.valueOf(100)))
                .thenReturn("razorpay-id");

        when(order.getTotalAmount()).thenReturn(BigDecimal.valueOf(100));

        String result = paymentService.createPayment(1L);

        assertEquals("razorpay-id", result);
        verify(order).markPaymentCreated("razorpay-id");
    }

    @Test
    void shouldThrowWhenOrderAlreadyPaid() {

        Order order = buildOrder(PaymentStatus.SUCCESS);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(InvalidStateException.class,
                () -> paymentService.createPayment(1L));
    }

    @Test
    void shouldReturnExistingOrderIdIfAlreadyCreated() {

        Order order = buildOrder(PaymentStatus.CREATED);
        when(order.getRazorpayOrderId()).thenReturn("existing-id");

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        String result = paymentService.createPayment(1L);

        assertEquals("existing-id", result);
    }

    @Test
    void shouldVerifyPaymentSuccessfully() {

        Order order = mock(Order.class);
        when(order.getPaymentStatus()).thenReturn(PaymentStatus.CREATED);
        when(order.getRazorpayOrderId()).thenReturn("razor-id");

        OrderItem item = mock(OrderItem.class);
        when(item.getProductId()).thenReturn(1L);
        when(item.getQuantity()).thenReturn(2);

        when(order.getOrderItems()).thenReturn(List.of(item));

        Product product = Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(100))
                .discountPercentage(null)
                .imageUrl(null)
                .featured(false)
                .status(ProductStatus.ACTIVE)
                .stock(10)
                .build();

        setId(product, 1L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(paymentGateway.verifyPayment("razor-id", "pay-id", "sig"))
                .thenReturn(true);

        paymentService.verifyPayment(
                1L,
                "razor-id",
                "pay-id",
                "sig"
        );

        verify(order).markPaymentSuccess("pay-id", "sig");
    }

    @Test
    void shouldThrowWhenSignatureInvalid() {

        Order order = buildOrder(PaymentStatus.CREATED);
        when(order.getRazorpayOrderId()).thenReturn("razor-id");

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(paymentGateway.verifyPayment(any(), any(), any()))
                .thenReturn(false);

        assertThrows(InvalidStateException.class,
                () -> paymentService.verifyPayment(
                        1L,
                        "razor-id",
                        "pay-id",
                        "sig"
                ));
    }

    @Test
    void shouldThrowWhenOrderNotFoundDuringCreate() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.createPayment(1L));
    }

    @Test
    void shouldThrowWhenOrderNotFoundDuringVerify() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.verifyPayment(
                        1L,
                        "razor-id",
                        "pay-id",
                        "sig"
                ));
    }

    @Test
    void shouldThrowWhenRazorpayOrderIdMismatch() {

        Order order = buildOrder(PaymentStatus.CREATED);
        when(order.getRazorpayOrderId()).thenReturn("correct-id");

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(InvalidStateException.class,
                () -> paymentService.verifyPayment(
                        1L,
                        "wrong-id",
                        "pay-id",
                        "sig"
                ));
    }

    @Test
    void shouldReturnIfPaymentAlreadySuccessful() {

        Order order = buildOrder(PaymentStatus.SUCCESS);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        paymentService.verifyPayment(
                1L,
                "razor-id",
                "pay-id",
                "sig"
        );

        verifyNoInteractions(paymentGateway);
    }

    private void setId(Object entity, Long id) {
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}