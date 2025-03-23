package com.casestudy.restaurantapi.service;

import com.casestudy.restaurantapi.dto.UserRegisterationRequest;
import com.casestudy.restaurantapi.model.User;

public interface UserServiceInterface {
    User registerUser(UserRegisterationRequest request);
    User login(String email, String password);
}