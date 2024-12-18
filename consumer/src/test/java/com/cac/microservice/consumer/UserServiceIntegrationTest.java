package com.cac.microservice.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetUser() {
        String baseUrl = "http://localhost:8124" ;
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/users/123", User.class);

        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertEquals("123", user.getId());
        assertEquals("John Doe", user.getName());
    }
}