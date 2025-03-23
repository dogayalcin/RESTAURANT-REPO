package com.casestudy.restaurantapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.casestudy.restaurantapi.dto.UserRegisterationRequest;
import com.casestudy.restaurantapi.enums.UserRole;
import com.casestudy.restaurantapi.model.User;
import com.casestudy.restaurantapi.service.UserServiceInterface;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceInterface userService;

    @Test
    @DisplayName("Should create user successfully")
    public void shouldCreateUserSuccessfully() throws Exception {
        User user = new User(10L, "dogayalcin", "doga@example.com", "123456", UserRole.COSTUMER, null);

        when(userService.registerUser(any(UserRegisterationRequest.class))).thenReturn(user);

        String requestBody = """
            {
                "username": "dogayalcin",
                "email": "doga@example.com",
                "password": "123456"
            }
            """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.username").value("dogayalcin"))
                .andExpect(jsonPath("$.email").value("doga@example.com"))
                .andExpect(jsonPath("$.role").value("COSTUMER"));
    }

    @Test
    @DisplayName("Should fail to create user if email already exists")
    public void shouldFailWhenEmailAlreadyExists() throws Exception {
        when(userService.registerUser(any(UserRegisterationRequest.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        String requestBody = """
            {
                "username": "dogayalcin",
                "email": "doga@example.com",
                "password": "123456"
            }
            """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    @DisplayName("Should login successfully as COSTUMER")
    public void shouldLoginAsCustomer() throws Exception {
        User user = new User(1L, "dogayalcin", "doga@example.com", "123456", UserRole.COSTUMER, null);

        when(userService.login("doga@example.com", "123456")).thenReturn(user);

        String requestBody = """
            {
              "email": "doga@example.com",
              "password": "123456"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("dogayalcin"))
                .andExpect(jsonPath("$.email").value("doga@example.com"))
                .andExpect(jsonPath("$.role").value("COSTUMER"));
    }

    @Test
    @DisplayName("Should login successfully as RESTAURANT")
    public void shouldLoginAsRestaurant() throws Exception {
        User user = new User(2L, "restaurantUser", "res@example.com", "123456", UserRole.RESTAURANT, null);

        when(userService.login("res@example.com", "123456")).thenReturn(user);

        String requestBody = """
            {
              "email": "res@example.com",
              "password": "123456"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("restaurantUser"))
                .andExpect(jsonPath("$.email").value("res@example.com"))
                .andExpect(jsonPath("$.role").value("RESTAURANT"));
    }

    @Test
    @DisplayName("Should fail login with wrong password")
    public void shouldFailWithWrongPassword() throws Exception {
        when(userService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        String requestBody = """
            {
              "email": "doga@example.com",
              "password": "wrongpass"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    @DisplayName("Should fail login with unknown email")
    public void shouldFailWithUnknownEmail() throws Exception {
        when(userService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("User not found"));

        String requestBody = """
            {
              "email": "unknown@example.com",
              "password": "123456"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }
}