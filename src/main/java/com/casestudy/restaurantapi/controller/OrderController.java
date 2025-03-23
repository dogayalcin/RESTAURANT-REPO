package com.casestudy.restaurantapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.restaurantapi.dto.CreateOrderRequest;
import com.casestudy.restaurantapi.dto.OrderResponse;
import com.casestudy.restaurantapi.dto.UpdateOrderRequest;
import com.casestudy.restaurantapi.service.OrderServiceInterface;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceInterface orderService;

    public OrderController(OrderServiceInterface orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<OrderResponse> postMethodName(@RequestBody UpdateOrderRequest request) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam Long userId) {
        List<OrderResponse> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }
}
