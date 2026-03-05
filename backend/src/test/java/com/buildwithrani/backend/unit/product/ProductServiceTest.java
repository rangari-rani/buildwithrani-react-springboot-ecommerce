package com.buildwithrani.backend.unit.product;

import com.buildwithrani.backend.audit.service.AuditService;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.common.cloudinary.CloudinaryService;
import com.buildwithrani.backend.common.enums.ProductStatus;
import com.buildwithrani.backend.common.exception.InvalidStateException;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.buildwithrani.backend.product.dto.ProductRequestDTO;
import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.entity.Product;
import com.buildwithrani.backend.product.mapper.ProductMapper;
import com.buildwithrani.backend.product.repository.ProductRepository;
import com.buildwithrani.backend.product.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private AuditService auditService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product buildProduct(ProductStatus status) {
        return Product.builder()
                .name("Laptop")
                .description("Gaming")
                .price(BigDecimal.valueOf(1000))
                .discountPercentage(null)
                .imageUrl(null)
                .featured(false)
                .status(status)
                .stock(10)
                .build();
    }

    @Test
    void shouldCreateProductWithImage() {

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Laptop");
        request.setDescription("Gaming Laptop");
        request.setPrice(BigDecimal.valueOf(1000));
        request.setStock(10);
        request.setFeatured(true);
        request.setDiscountPercentage(10);

        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(cloudinaryService.uploadImage(image)).thenReturn("image-url");

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        when(productMapper.toResponse(any(Product.class))).thenReturn(responseDTO);

        ProductResponseDTO result = productService.createProduct(request, image);

        assertNotNull(result);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product savedProduct = captor.getValue();
        assertEquals("Laptop", savedProduct.getName());
        assertEquals(ProductStatus.INACTIVE, savedProduct.getStatus());
        assertEquals(10, savedProduct.getStock());

        verify(cloudinaryService).uploadImage(image);
    }

    @Test
    void shouldCreateProductWithoutImage() {

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Laptop");
        request.setPrice(BigDecimal.valueOf(1000));
        request.setStock(5);

        when(productMapper.toResponse(any(Product.class)))
                .thenReturn(new ProductResponseDTO());

        productService.createProduct(request, null);

        verify(productRepository).save(any(Product.class));
        verify(cloudinaryService, never()).uploadImage(any());
    }

    @Test
    void shouldUpdateProductSuccessfully() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponseDTO());

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Updated Name");

        ProductResponseDTO result =
                productService.updateProduct(1L, request, null);

        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDiscontinuedProduct() {

        Product product = buildProduct(ProductStatus.DISCONTINUED);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InvalidStateException.class,
                () -> productService.updateProduct(1L,
                        new ProductRequestDTO(),
                        null));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(1L,
                        new ProductRequestDTO(),
                        null));
    }

    @Test
    void shouldUpdateProductStatusAndCallAudit() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProductStatus(1L, ProductStatus.INACTIVE);

        assertEquals(ProductStatus.INACTIVE, product.getStatus());

        verify(auditService).logAction(
                any(),
                any(),
                eq("PRODUCT_STATUS_CHANGE"),
                eq("PRODUCT"),
                any(),
                any()
        );
    }

    @Test
    void shouldGetProductById() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponseDTO());

        ProductResponseDTO result =
                productService.getProductById(1L);

        assertNotNull(result);
        verify(productRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenProductNotFoundById() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1L));
    }

    @Test
    void shouldReturnActiveProducts() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository
                .findByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE))
                .thenReturn(List.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponseDTO());

        List<ProductResponseDTO> result =
                productService.getActiveProducts();

        assertEquals(1, result.size());

        verify(productRepository)
                .findByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE);
    }

    @Test
    void shouldUpdateFeaturedStatus() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.updateFeaturedStatus(1L, true);

        assertTrue(product.isFeatured());
    }

    @Test
    void shouldThrowWhenUpdatingFeaturedOnDiscontinuedProduct() {

        Product product = buildProduct(ProductStatus.DISCONTINUED);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(InvalidStateException.class,
                () -> productService.updateFeaturedStatus(1L, true));
    }

    @Test
    void shouldIncreaseStock() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.increaseStock(1L, 5);

        assertEquals(15, product.getStock());
    }

    @Test
    void shouldDecreaseStock() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.decreaseStock(1L, 5);

        assertEquals(5, product.getStock());
    }

    @Test
    void shouldThrowWhenDecreasingStockTooMuch() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(InvalidStateException.class,
                () -> productService.decreaseStock(1L, 50));
    }

    @Test
    void shouldReturnAllProducts() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository.findAll())
                .thenReturn(List.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponseDTO());

        List<ProductResponseDTO> result =
                productService.getAllProducts();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFeaturedProducts() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository
                .findTop5ByFeaturedTrueAndStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE))
                .thenReturn(List.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponseDTO());

        List<ProductResponseDTO> result =
                productService.getFeaturedProducts();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnNewArrivals() {

        Product product = buildProduct(ProductStatus.ACTIVE);

        when(productRepository
                .findTop5ByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE))
                .thenReturn(List.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponseDTO());

        List<ProductResponseDTO> result =
                productService.getNewArrivals();

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenUpdatingStatusOfDiscontinuedProduct() {

        Product product = buildProduct(ProductStatus.DISCONTINUED);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(InvalidStateException.class,
                () -> productService.updateProductStatus(1L, ProductStatus.ACTIVE));
    }
}
