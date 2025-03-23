package com.casestudy.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.casestudy.restaurantapi.enums.UserRole;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    //Bir User, birden cok Order verebilir.
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

}