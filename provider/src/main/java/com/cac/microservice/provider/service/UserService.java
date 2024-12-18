package com.cac.microservice.provider.service;

import com.cac.microservice.provider.entity.User;
import com.cac.microservice.provider.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user
    public User createUser(User user) {
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with this ID already exists");
        }
        return userRepository.save(user); // Save the user in the repository
    }

    // Get a user by ID
    public User getUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // Update an existing user
    public User updateUser(String userId, User updatedUser) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        updatedUser.setId(userId); // Ensure the ID stays the same
        return userRepository.save(updatedUser);
    }

    // Delete a user by ID
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId); // Delete the user
    }
}
