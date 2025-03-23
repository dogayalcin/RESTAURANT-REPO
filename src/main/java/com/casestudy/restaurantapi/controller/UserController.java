package com.casestudy.restaurantapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.restaurantapi.model.User;
import com.casestudy.restaurantapi.service.UserServiceInterface;

import jakarta.validation.Valid;

import com.casestudy.restaurantapi.dto.UserLoginRequest;
import com.casestudy.restaurantapi.dto.UserRegisterationRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceInterface userService;

    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegisterationRequest request) {
        User savedUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserLoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}