package com.casestudy.restaurantapi.service;

import com.casestudy.restaurantapi.dto.UserRegisterationRequest;
import com.casestudy.restaurantapi.enums.UserRole;
import com.casestudy.restaurantapi.model.User;
import com.casestudy.restaurantapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserServiceInterface userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        UserRegisterationRequest request = new UserRegisterationRequest();
        request.setUsername("dogayalcin");
        request.setEmail("doga@example.com");
        request.setPassword("1234");

        when(userRepository.existsByEmail("doga@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("dogayalcin")).thenReturn(false);

        User user = new User();
        user.setId(1L);
        user.setUsername("dogayalcin");
        user.setEmail("doga@example.com");
        user.setPassword("1234");
        user.setRole(UserRole.COSTUMER);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.registerUser(request);

        assertEquals("dogayalcin", saved.getUsername());
        assertEquals(UserRole.COSTUMER, saved.getRole());
    }

    @Test
    @DisplayName("Should throw if email already exists")
    void shouldThrowIfEmailExists() {
        UserRegisterationRequest request = new UserRegisterationRequest();
        request.setUsername("dogayalcin");
        request.setEmail("doga@example.com");
        request.setPassword("1234");

        when(userRepository.existsByEmail("doga@example.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw if username already exists")
    void shouldThrowIfUsernameExists() {
        UserRegisterationRequest request = new UserRegisterationRequest();
        request.setUsername("dogayalcin");
        request.setEmail("doga@example.com");
        request.setPassword("1234");

        when(userRepository.existsByEmail("doga@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("dogayalcin")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Username already exists", ex.getMessage());
    }

    @Test
    @DisplayName("Should login successfully")
    void shouldLoginSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setUsername("dogayalcin");
        user.setEmail("doga@example.com");
        user.setPassword("1234");
        user.setRole(UserRole.COSTUMER);

        when(userRepository.findByEmail("doga@example.com")).thenReturn(Optional.of(user));

        User result = userService.login("doga@example.com", "1234");

        assertEquals("dogayalcin", result.getUsername());
        assertEquals("1234", result.getPassword());
    }

    @Test
    @DisplayName("Should throw if user not found with email")
    void shouldThrowIfEmailNotFound() {
        when(userRepository.findByEmail("noone@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.login("noone@example.com", "anypass");
        });

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw if password is incorrect")
    void shouldThrowIfPasswordIncorrect() {
        User user = new User();
        user.setId(1L);
        user.setUsername("dogayalcin");
        user.setEmail("doga@example.com");
        user.setPassword("1234");
        user.setRole(UserRole.COSTUMER);

        when(userRepository.findByEmail("doga@example.com")).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.login("doga@example.com", "wrongpass");
        });

        assertEquals("Your email and/or password is wrong, please try again", ex.getMessage());
    }
}