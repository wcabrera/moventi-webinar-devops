package com.webinar.moventiwebinardevops.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HelloControllerTest {

    private final HelloController controller = new HelloController();

    @Test
    void shouldReturnHelloMessage() {

        var response = controller.hello("William");

        assertEquals(
                "Hola William",
                response.get("message")
        );
    }

    @Test
    void shouldReturnCurrentTime() {

        var response = controller.time();

        assertNotNull(response.get("timestamp"));
    }
}
