package com.casestudy.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_items")
public class MenuItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private double price;
    private String description;
    private String category;
    
    // menu itemler birden cok gunun menusunde olabilir
    // entity icinde gunlerin listesini tutmak icin
    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "menu_item_days", joinColumns = @JoinColumn(name = "menu_id"))
    @Column(name = "day_of_week")
    private Set<DayOfWeek> days;
}
