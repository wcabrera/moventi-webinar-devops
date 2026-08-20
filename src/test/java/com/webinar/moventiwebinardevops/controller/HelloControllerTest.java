package com.webinar.moventiwebinardevops.controller;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    private final HelloController controller = new HelloController();

    @Test
    void hello_shouldReturnMessageWithName() {

        Map<String, Object> response = controller.hello("William");

        assertEquals("Hola William", response.get("message"));
        assertEquals("DevOps Moventi 20260820", response.get("application"));
        assertEquals("1.0.0", response.get("version"));

        assertNotNull(response.get("timestamp"));
    }

    @Test
    void time_shouldReturnTimestamp() {

        Map<String, Object> response = controller.time();

        assertNotNull(response.get("timestamp"));
    }

    @Test
    void hash_shouldGenerateMd5Hash() throws Exception {

        Map<String, Object> response = controller.hash("admin");

        assertEquals("admin", response.get("password"));
        assertNotNull(response.get("hash"));

        assertEquals(
                "21232f297a57a5a743894a0e4a801fc3",
                response.get("hash")
        );
    }

    @Test
    void generateToken_shouldReturnToken() {

        Map<String, Object> response = controller.generateToken();

        assertNotNull(response.get("token"));
        assertNotNull(response.get("timestamp"));

        int token = (Integer) response.get("token");

        assertTrue(token >= 0);
        assertTrue(token < 999999);
    }

    @Test
    void config_shouldExposeConfiguredValues() {

        Map<String, Object> response = controller.config();

        assertEquals("admin", response.get("username"));
        assertEquals("Admin123456", response.get("password"));
        assertEquals(
                "sk-devops-webinar-123456789",
                response.get("apiKey")
        );

        assertEquals(
                "development",
                response.get("environment")
        );
    }
}