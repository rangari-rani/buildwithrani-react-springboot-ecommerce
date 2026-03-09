package com.buildwithrani.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadController {

    @GetMapping("/inspect-thread")
    public String getThreadInfo() {
        // This will return the name of the thread handling the request
        // If Virtual Threads are enabled, you'll see "VirtualThread" in the output
        return "Current Thread: " + Thread.currentThread().toString();
    }
}
