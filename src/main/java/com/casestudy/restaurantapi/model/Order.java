package com.casestudy.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.casestudy.restaurantapi.enums.OrderStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
