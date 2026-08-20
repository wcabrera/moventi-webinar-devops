package com.webinar.moventiwebinardevops.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    // VULNERABILIDAD 1:
    // Credenciales hardcodeadas
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "Admin123456";
    private static final String API_KEY = "sk-devops-webinar-123456789";

    @GetMapping("/hello")
    public Map<String, Object> hello(
            @RequestParam(defaultValue = "DevOps") String name) {

        return Map.of(
                "message", "Hola " + name,
                "application", "DevOps Moventi 20260820",
                "version", "1.0.0",
                "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/time")
    public Map<String, Object> time() {

        return Map.of(
                "timestamp", Instant.now().toString()
        );
    }

    // VULNERABILIDAD 2:
    // Uso de algoritmo criptográfico débil: MD5
    @GetMapping("/hash")
    public Map<String, Object> hash(
            @RequestParam String password) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] hash = md.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return Map.of(
                "password", password,
                "hash", result.toString()
        );
    }

    // VULNERABILIDAD 3:
    // Random no criptográficamente seguro
    @GetMapping("/token")
    public Map<String, Object> generateToken() {

        Random random = new Random();

        int token = random.nextInt(999999);

        return Map.of(
                "token", token,
                "timestamp", Instant.now().toString()
        );
    }

    // VULNERABILIDAD 4:
    // Exposición de información sensible
    @GetMapping("/config")
    public Map<String, Object> config() {

        return Map.of(
                "username", ADMIN_USER,
                "password", ADMIN_PASSWORD,
                "apiKey", API_KEY,
                "environment", "development"
        );
    }
}