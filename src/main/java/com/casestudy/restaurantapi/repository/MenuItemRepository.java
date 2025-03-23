package com.casestudy.restaurantapi.repository;

import com.casestudy.restaurantapi.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByDaysContaining(DayOfWeek day);
}
