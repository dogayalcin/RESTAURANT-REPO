package com.casestudy.restaurantapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.casestudy.restaurantapi.dto.CreateOrderRequest;
import com.casestudy.restaurantapi.dto.OrderItemRequest;
import com.casestudy.restaurantapi.dto.OrderItemResponse;
import com.casestudy.restaurantapi.dto.OrderResponse;
import com.casestudy.restaurantapi.dto.UpdateOrderRequest;
import com.casestudy.restaurantapi.enums.OrderStatus;
import com.casestudy.restaurantapi.enums.UserRole;
import com.casestudy.restaurantapi.model.MenuItem;
import com.casestudy.restaurantapi.model.Order;
import com.casestudy.restaurantapi.model.OrderItem;
import com.casestudy.restaurantapi.model.User;
import com.casestudy.restaurantapi.repository.MenuItemRepository;
import com.casestudy.restaurantapi.repository.OrderRepository;
import com.casestudy.restaurantapi.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class OrderService implements OrderServiceInterface{

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.IN_PROGRESS);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setOrderId(order.getId());
        dto.setStatus(order.getStatus().name());

        List<OrderItemResponse> itemDTOs = order.getItems().stream().map(item -> {
            OrderItemResponse i = new OrderItemResponse();
            i.setItemName(item.getMenuItem().getName());
            i.setQuantity(item.getQuantity());
            return i;
        }).toList();

        dto.setItems(itemDTOs);
        return dto;
    }

    public OrderResponse updateOrderStatus(UpdateOrderRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(user.getRole() == UserRole.RESTAURANT){
           Order order = orderRepository.findById(request.getOrder().getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
           order.setStatus(request.getStatus());
           return mapToOrderResponse(orderRepository.save(order));
        }
        else{
            throw new RuntimeException("Not authorized");
        }
    }

    public List<OrderResponse> getOrdersForUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        List<Order> orders;
    
        if (user.getRole() == UserRole.RESTAURANT) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByUserId(userId);
        }
        
        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : orders) {
            OrderResponse dto = mapToOrderResponse(order);
            responseList.add(dto);
        }
        return responseList;
    }
}