package com.casestudy.restaurantapi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.casestudy.restaurantapi.dto.UserRegisterationRequest;
import com.casestudy.restaurantapi.enums.UserRole;
import com.casestudy.restaurantapi.model.User;
import com.casestudy.restaurantapi.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserRegisterationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
    
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }    
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(UserRole.COSTUMER);

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
    
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
    
        User user = userOpt.get();
    
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Your email and/or password is wrong, please try again");
        }
        
        return user;
    }
}
