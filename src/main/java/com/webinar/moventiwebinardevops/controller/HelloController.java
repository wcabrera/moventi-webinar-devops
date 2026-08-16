package com.webinar.moventiwebinardevops.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    private static final String DB_PASSWORD = "admin123";

    @GetMapping("/hello")
    public Map<String, Object> hello(
            @RequestParam(defaultValue = "DevOps") String name) {

        return Map.of(
                "message", "Hola " + name,
                "application", "DevOps Webinar 2026",
                "version", "1.0.0",
                "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/hello-legacy")
    public Map<String, Object> helloLegacy(
            @RequestParam(defaultValue = "DevOps") String name) {

        return Map.of(
                "message", "Hola " + name,
                "application", "DevOps Webinar 2026",
                "version", "1.0.0",
                "timestamp", Instant.now().toString()
        );
    }
}
