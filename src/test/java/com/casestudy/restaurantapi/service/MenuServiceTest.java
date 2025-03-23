package com.casestudy.restaurantapi.service;

import com.casestudy.restaurantapi.model.MenuItem;
import com.casestudy.restaurantapi.repository.MenuItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuServiceTest {

    private MenuServiceInterface menuService;
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    void setUp() {
        menuItemRepository = mock(MenuItemRepository.class);
        menuService = new MenuService(menuItemRepository);
    }

    @Test
    @DisplayName("Should return today's menu items")
    void shouldReturnTodayItems() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        MenuItem item1 = new MenuItem();
        item1.setId(1L);
        item1.setDays(Set.of(today));

        when(menuItemRepository.findByDaysContaining(today)).thenReturn(List.of(item1));

        List<MenuItem> result = menuService.getTodayMenu();

        assertEquals(1, result.size());
        assertTrue(result.contains(item1));
    }

    @Test
    @DisplayName("Should return empty list if no item is available for today")
    void shouldReturnEmptyListIfNoMatch() {

        when(menuItemRepository.findAll()).thenReturn(null);

        List<MenuItem> result = menuService.getTodayMenu();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}