package com.casestudy.restaurantapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;
}
