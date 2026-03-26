package com.buildwithrani.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadController {

    @GetMapping("/inspect-thread")
    public String getThreadInfo() {
        return "Current Thread: " + Thread.currentThread().toString();
    }
}
