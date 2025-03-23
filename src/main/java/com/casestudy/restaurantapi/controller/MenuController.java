package com.casestudy.restaurantapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.restaurantapi.model.MenuItem;
import com.casestudy.restaurantapi.service.MenuServiceInterface;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    
    private final MenuServiceInterface menuService;

    public MenuController(MenuServiceInterface menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/today")
    public List<MenuItem> getTodayMenu() {
        return menuService.getTodayMenu();
    }
}
