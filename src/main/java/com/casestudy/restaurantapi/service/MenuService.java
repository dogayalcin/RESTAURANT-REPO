package com.casestudy.restaurantapi.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.casestudy.restaurantapi.model.MenuItem;
import com.casestudy.restaurantapi.repository.MenuItemRepository;

@Service
public class MenuService implements MenuServiceInterface{

    private final MenuItemRepository menuItemRepository;
    
    public MenuService(MenuItemRepository menuRepository) {
        this.menuItemRepository = menuRepository;
    }

    public List<MenuItem> getTodayMenu() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return menuItemRepository.findByDaysContaining(today);
    }

}
