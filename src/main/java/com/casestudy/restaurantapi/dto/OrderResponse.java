package com.casestudy.restaurantapi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    
    private Long orderId;
    
    private List<OrderItemResponse> items;
    private String status;
}
