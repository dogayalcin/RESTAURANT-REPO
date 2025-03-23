package com.casestudy.restaurantapi.controller;

import com.casestudy.restaurantapi.model.MenuItem;
import com.casestudy.restaurantapi.service.MenuServiceInterface;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Sadece MenuController test
@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    //controller’a HTTP istegi
    @Autowired
    private MockMvc mockMvc;

    //mock service
    @MockBean
    private MenuServiceInterface menuService;

    @Test
    @DisplayName("Should return today's MenuItem when items exist")
    public void shouldReturnTodayMenuItem_WhenMenuItemsExist() throws Exception {

        List<MenuItem> todayMenuItems = Arrays.asList(
                new MenuItem(1L, "Tartine Lunch Box with Mozzarella", 64.0, "Tartine with sourdough, mozzarella, zucchini, tomato, hummus, salad, vegan chocolate", "DISHES & APPETIZERS",Set.of(DayOfWeek.SATURDAY)),
                new MenuItem(2L, "Refreshing Green Vegetable Juice", 25.0, "Spinach, mint, apple, cucumber, lemon", "DRINKS", Set.of(DayOfWeek.SATURDAY))
        );

        when(menuService.getTodayMenu()).thenReturn(todayMenuItems);

        mockMvc.perform(get("/api/menu/today")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Tartine Lunch Box with Mozzarella"))
                .andExpect(jsonPath("$[1].name").value("Refreshing Green Vegetable Juice"));
    }

    @Test
    @DisplayName("Should return empty list when no MenuItem exists for today")
    public void shouldReturnEmptyList_WhenNoMenuItemExists() throws Exception {
        when(menuService.getTodayMenu()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/menu/today")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(0));
    }
}