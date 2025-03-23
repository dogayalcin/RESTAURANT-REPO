package com.casestudy.restaurantapi.dto;

import lombok.Getter;
import lombok.Setter;

import com.casestudy.restaurantapi.enums.OrderStatus;
import com.casestudy.restaurantapi.model.Order;

@Getter
@Setter
public class UpdateOrderRequest {
    private Long userId;
    private Order order;
    private OrderStatus status;
}
