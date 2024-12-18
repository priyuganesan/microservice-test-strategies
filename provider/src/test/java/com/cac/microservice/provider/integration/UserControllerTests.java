package com.cac.microservice.provider.integration;

import com.cac.microservice.provider.entity.User;
import com.cac.microservice.provider.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUser_Success() throws Exception {
        String userId = "123";
        User user = new User(userId, "John Doe");
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testSaveUser() throws Exception {
        User userToSave = new User("123", "Jane Doe");
        when(userService.createUser(any(User.class))).thenReturn(userToSave);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("{\"id\":\"123\",\"name\":\"Jane Doe\"}"))
                .andExpect(status().isCreated())  // Expecting HTTP 201 Created
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }
}