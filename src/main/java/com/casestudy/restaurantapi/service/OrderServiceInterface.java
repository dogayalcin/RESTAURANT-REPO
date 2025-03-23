package com.casestudy.restaurantapi.service;

import com.casestudy.restaurantapi.dto.CreateOrderRequest;
import com.casestudy.restaurantapi.dto.UpdateOrderRequest;
import com.casestudy.restaurantapi.dto.OrderResponse;

import java.util.List;

public interface OrderServiceInterface {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse updateOrderStatus(UpdateOrderRequest request);
    List<OrderResponse> getOrdersForUser(Long userId);
}