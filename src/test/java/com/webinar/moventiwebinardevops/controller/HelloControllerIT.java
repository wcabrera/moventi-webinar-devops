package com.webinar.moventiwebinardevops.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerIT {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void contextRespondsOverHttp() {
        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/hello?name=William",
                String.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("Hola William"));
    }
}
