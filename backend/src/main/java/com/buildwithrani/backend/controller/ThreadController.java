package com.buildwithrani.backend.controller;

import com.buildwithrani.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class ThreadController {

    private final ProductService productService;

    @PostMapping("/concurrency/{productId}")
    public ResponseEntity<?> runConcurrencyTest(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "10") int threadCount,
            @RequestParam(defaultValue = "redis") String strategy
    ) {
        // Create a pool of threads to simulate simultaneous users
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    if ("redis".equalsIgnoreCase(strategy)) {
                        productService.updateStockWithRedisLock(productId, 1);
                    } else {
                        productService.decreaseStock(productId, 1); // Your Atomic/Pessimistic way
                    }
                } catch (Exception e) {
                    System.err.println("Thread error: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return ResponseEntity.ok(Map.of(
                "message", "Concurrency test completed using " + strategy + " strategy",
                "threadsSimulated", threadCount
        ));
    }

    @GetMapping("/inspect-thread")
    public String getThreadInfo() {
        return "Backend active. Current Thread: " + Thread.currentThread().getName();
    }
}