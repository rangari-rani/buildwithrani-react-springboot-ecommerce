package com.buildwithrani.backend.product.controller;

import com.buildwithrani.backend.product.dto.ProductResponseDTO;
import com.buildwithrani.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // -------- USER: Product List --------
    @GetMapping
    public List<ProductResponseDTO> getAllActiveProducts() {
        return productService.getActiveProducts();
    }

    // -------- USER: Product Detail --------
    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
