package com.casestudy.restaurantapi.service;

import com.casestudy.restaurantapi.dto.CreateOrderRequest;
import com.casestudy.restaurantapi.dto.OrderItemRequest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderServiceInterface orderService;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        menuItemRepository = mock(MenuItemRepository.class);
        orderService = new OrderService(orderRepository, menuItemRepository, userRepository);
    }

    @Test
    @DisplayName("Should create order successfully for valid customer and menu items")
    void shouldCreateOrderSuccessfully() {
        User user = new User(1L, "costumertest", "ct@mail.com", "123", UserRole.COSTUMER, new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        MenuItem item1 = new MenuItem(1L, "", 50.0, "", "DRINKS", null);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item1));

        Order savedOrder = new Order();
        savedOrder.setId(100L);
        savedOrder.setUser(user);
        savedOrder.setStatus(OrderStatus.IN_PROGRESS);
        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItem(item1);
        orderItem.setQuantity(2);
        orderItem.setPriceSnapshot(item1.getPrice());
        orderItem.setOrder(savedOrder);
        savedOrder.setItems(List.of(orderItem));

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setItems(List.of(new OrderItemRequest(1L, 2)));

        OrderResponse response = orderService.createOrder(request);

        assertEquals(100L, response.getOrderId());
        assertEquals("IN_PROGRESS", response.getStatus());
        assertEquals(1, response.getItems().size());
        assertEquals(2, response.getItems().get(0).getQuantity());
    }

    @Test
    @DisplayName("Should throw exception if user is not found")
    void shouldThrowIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setItems(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    @DisplayName("Should get orders for a customer user")
    void shouldReturnOrdersForCustomer() {
        User user = new User(1L, "costumertest", "ct@mail.com", "123", UserRole.COSTUMER, new ArrayList<>());
        User user2 = new User(15L,"test1","test1@mail.com","test1password",UserRole.COSTUMER, new ArrayList<>());
        
        Order order = new Order();
        order.setId(10L);
        order.setStatus(OrderStatus.APPROVED);
        order.setUser(user);
        order.setItems(new ArrayList<>());
        Order order2 = new Order();
        order2.setId(21L);
        order2.setStatus(OrderStatus.IN_PROGRESS);
        order2.setUser(user2);
        order2.setItems(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));
        when(userRepository.findById(15L)).thenReturn(Optional.of(user2));
        when(orderRepository.findByUserId(15L)).thenReturn(List.of(order2));

        List<OrderResponse> responses = orderService.getOrdersForUser(1L);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should get all orders for a RESTAURANT user")
    void shouldReturnAllOrdersForRestaurant() {
        User user = new User(2L, "restauranttest", "resttest@mail.com", "456", UserRole.RESTAURANT, new ArrayList<>());
        User user2 = new User(15L,"test1","test1@mail.com","test1password",UserRole.COSTUMER, new ArrayList<>());
        User user3 = new User(25L,"test2","test2@mail.com","test2password",UserRole.COSTUMER, new ArrayList<>());


        Order order1 = new Order();
        order1.setId(20L);
        order1.setStatus(OrderStatus.DELIVERED);
        order1.setUser(user2);
        order1.setItems(new ArrayList<>());
        Order order2 = new Order();
        order2.setId(21L);
        order2.setStatus(OrderStatus.IN_PROGRESS);
        order2.setUser(user3);
        order2.setItems(new ArrayList<>());

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<OrderResponse> responses = orderService.getOrdersForUser(2L);
        assertEquals(2, responses.size());
    }

    @Test
    @DisplayName("Should update order status if user is RESTAURANT")
    void shouldUpdateOrderStatusIfRestaurant() {
        User restaurantUser = new User(5L, "res", "r@r.com", "xxx", UserRole.RESTAURANT, new ArrayList<>());
        User costumer = new User(4L, "cust", "x@x.com", "pass", UserRole.COSTUMER, new ArrayList<>());
        
        Order order = new Order();
        order.setId(15L);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setUser(costumer);
        order.setItems(new ArrayList<>());

        when(userRepository.findById(5L)).thenReturn(Optional.of(restaurantUser));
        when(userRepository.findById(4L)).thenReturn(Optional.of(costumer));
        when(orderRepository.findById(15L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setUserId(5L);
        request.setOrder(order);
        request.setStatus(OrderStatus.DELIVERED);

        OrderResponse response = orderService.updateOrderStatus(request);

        assertEquals("DELIVERED", response.getStatus());
    }

    @Test
    @DisplayName("Should throw if unauthorized user tries to update order")
    void shouldThrowIfUnauthorizedUserTriesToUpdate() {
        User customer = new User(4L, "cust", "x@x.com", "pass", UserRole.COSTUMER, new ArrayList<>());
        when(userRepository.findById(4L)).thenReturn(Optional.of(customer));

        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setUserId(4L);
        request.setOrder(new Order());
        request.setStatus(OrderStatus.APPROVED);

        Exception ex = assertThrows(RuntimeException.class, () -> orderService.updateOrderStatus(request));
        assertEquals("Not authorized", ex.getMessage());
    }
}