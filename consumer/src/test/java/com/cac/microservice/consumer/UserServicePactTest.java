package com.cac.microservice.consumer;

//import au.com.dius.pact.consumer.MockServer;
//import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
//import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
//import au.com.dius.pact.consumer.junit5.PactConsumerTest;
//import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
//import au.com.dius.pact.consumer.junit5.PactTestFor;
//import au.com.dius.pact.core.model.RequestResponsePact;
//import au.com.dius.pact.core.model.V4Pact;
//import au.com.dius.pact.core.model.annotations.Pact;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.DefaultUriBuilderFactory;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(PactConsumerTestExt.class)
//@PactTestFor(providerName = "Provider")
//public class UserServicePactTest {
//
//    @Pact(consumer = "Consumer", provider = "Provider")
//    public V4Pact createPact(PactDslWithProvider builder) {
//        return builder
//                .given("User with ID 123 exists")
//                .uponReceiving("A request to get user with ID 123")
//                .path("/users/123")
//                .method("GET")
//                .willRespondWith()
//                .status(200)
//                .body(new PactDslJsonBody()
//                        .stringType("id", "123")
//                        .stringType("name", "John Doe"))
//                .toPact(V4Pact.class);
//    }
//    @Test
//    void testGetUserFromProvider(MockServer mockServer) {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(mockServer.getUrl()));
//        UserService userService = new UserService(restTemplate);
//        User user = userService.getUser("123");
//        assertEquals("123", user.getId());
//        assertEquals("John Doe", user.getName());
//    }
//}

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "provider")
@SpringBootTest
public class UserServicePactTest {

    @Autowired
    private RestTemplate restTemplate;

    @Pact(consumer = "consumer", provider = "provider")
    public V4Pact createGetUserPact(PactDslWithProvider builder) {
        PactDslJsonBody getResponseBody = new PactDslJsonBody()
                .stringType("id", "123")
                .stringType("name", "John Doe");

        return builder
                .given("User with ID 123 exists")
                .uponReceiving("A request to get user with ID 123")
                .path("/users/123")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(getResponseBody)
                .toPact(V4Pact.class);
    }

    @Pact(consumer = "consumer", provider = "provider")
    public V4Pact createPostUserPact(PactDslWithProvider builder) {
        PactDslJsonBody postRequestBody = new PactDslJsonBody()
                .stringType("id", "124")
                .stringType("name", "Jane Doe");

        PactDslJsonBody postResponseBody = new PactDslJsonBody()
                .stringType("id", "124")
                .stringType("name", "Jane Doe");

        return builder
                .uponReceiving("A request to create user with ID 124")
                .path("/users")
                .method("POST")
                .headers("Content-Type", "application/json; charset=UTF-8")
                .body(postRequestBody)
                .willRespondWith()
                .status(200)
                .body(postResponseBody)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createGetUserPact")
    void testGetUserFromProvider(MockServer mockServer) {
        // Set the URI for the mock server
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(mockServer.getUrl()));

        // Now pass the RestTemplate into UserService with the mock server URL
        UserService userService = new UserService(restTemplate, mockServer.getUrl());

        // Perform the GET request
        User user = userService.getUser("123");

        // Assert the response
        assertEquals("123", user.getId());
        assertEquals("John Doe", user.getName());
    }

    @Test
    @PactTestFor(pactMethod = "createPostUserPact")
    void testSaveUserToProvider(MockServer mockServer) {
        // Set the URI for the mock server
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(mockServer.getUrl()));

        // Now pass the RestTemplate into UserService with the mock server URL
        UserService userService = new UserService(restTemplate, mockServer.getUrl());

        // Prepare a user to save
        User userToSave = new User("124", "Jane Doe");

        // Perform the POST request
        User createdUser = userService.saveUser(userToSave);

        // Assert the response
        assertEquals("124", createdUser.getId());
        assertEquals("Jane Doe", createdUser.getName());
    }
}

