package com.cac.microservice.provider.unit.solitary;

import com.cac.microservice.provider.UserController;
import com.cac.microservice.provider.entity.User;
import com.cac.microservice.provider.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUser_Success() {
        String userId = "123";
        User user = new User(userId, "John Doe");
        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(userId);

        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        assertEquals("John Doe", response.getBody().getName());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testGetUser_NotFound() {
        String userId = "999";
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("User not found"));

        ResponseEntity<User> response = userController.getUser(userId);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testSaveUser() {
        User userToSave = new User("123", "Jane Doe");
        when(userService.createUser(userToSave)).thenReturn(userToSave);

        ResponseEntity<User> response = userController.createUser(userToSave);

        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().getId());
        assertEquals("Jane Doe", response.getBody().getName());
        assertEquals(201, response.getStatusCodeValue());

    }
}