package com.cac.microservice.provider.component;


import com.cac.microservice.provider.UserController;
import com.cac.microservice.provider.entity.User;
import com.cac.microservice.provider.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerComponentTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUser() throws Exception {
        String userId = "1";
        User mockUser = new User(userId, "Test User");
        when(userService.getUserById(userId)).thenReturn(mockUser);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testCreateUser() throws Exception {
        User userToSave = new User("2", "New User");
        when(userService.createUser(any(User.class))).thenReturn(userToSave);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("{\"id\":\"2\",\"name\":\"New User\"}"))
                .andExpect(status().isCreated())  // Expecting HTTP 201 Created
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("New User"));
    }
}
