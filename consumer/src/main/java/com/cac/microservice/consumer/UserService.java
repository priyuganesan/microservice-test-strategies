package com.cac.microservice.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UserService(RestTemplate restTemplate, @Value("${base.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public User getUser(String id) {
        return restTemplate.getForObject(baseUrl + "/users/{id}", User.class, id);
    }

    public User saveUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        return restTemplate.postForObject(baseUrl + "/users", request, User.class);
    }
}

@Data
@AllArgsConstructor
class User {
    private String id;
    private String name;

}