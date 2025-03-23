package com.casestudy.restaurantapi.service;

import com.casestudy.restaurantapi.model.MenuItem;

import java.util.List;

public interface MenuServiceInterface {
    List<MenuItem> getTodayMenu();
}