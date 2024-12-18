package com.cac.microservice.provider.unit.sociable;

import com.cac.microservice.provider.entity.User;
import com.cac.microservice.provider.repository.UserRepository;
import com.cac.microservice.provider.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EntityManager entityManager;

    @Test
    public void testGetUser_Success() throws Exception {
        String userId = "123";
        User user = new User(userId, "John Doe");
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService).getUserById(userId);
    }

    @Test
    public void testSaveUser() throws Exception {
        User userToSave = new User("123", "Jane Doe");
        when(userService.createUser(any(User.class))).thenReturn(userToSave);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("{\"id\":\"123\",\"name\":\"Jane Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(header().string("Content-Type", "application/json"));
        verify(userService, times(1)).createUser(userToSave); // Verifying that the createUser method was called exactly once
    }

    @Test
    public void testGetUser_NotFound() throws Exception {
        String userId = "123";
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}