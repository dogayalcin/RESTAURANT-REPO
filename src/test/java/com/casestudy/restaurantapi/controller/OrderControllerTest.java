package com.casestudy.restaurantapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.casestudy.restaurantapi.dto.OrderItemResponse;
import com.casestudy.restaurantapi.dto.OrderResponse;
import com.casestudy.restaurantapi.dto.UpdateOrderRequest;
import com.casestudy.restaurantapi.enums.OrderStatus;
import com.casestudy.restaurantapi.model.Order;
import com.casestudy.restaurantapi.repository.OrderRepository;
import com.casestudy.restaurantapi.repository.UserRepository;
import com.casestudy.restaurantapi.service.OrderServiceInterface;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceInterface orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should create order and return response")
    public void shouldCreateOrderSuccessfully() throws Exception {

        OrderResponse response = new OrderResponse();
        response.setOrderId(1L);
        response.setStatus(OrderStatus.IN_PROGRESS.name());
        response.setItems(List.of(
                new OrderItemResponse("Latte", 2),
                new OrderItemResponse("Croissant", 1)
        ));

        //mock servis
        when(orderService.createOrder(org.mockito.ArgumentMatchers.any()))
                .thenReturn(response);

        String requestBody = """
        {
          "userId": 1,
          "items": [
            { "menuItemId": 2, "quantity": 2 },
            { "menuItemId": 3, "quantity": 1 }
          ]
        }
        """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].itemName").value("Latte"))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    @DisplayName("Should update order status for restaurant user")
    public void shouldUpdateOrderStatusSuccessfully() throws Exception {
        Long orderId = 1L;
        Long userId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.IN_PROGRESS);

        OrderResponse response = new OrderResponse();
        response.setOrderId(userId);
        response.setItems(List.of(
                new OrderItemResponse("Latte", 2),
                new OrderItemResponse("Croissant", 1)
        ));
        response.setStatus(OrderStatus.DELIVERED.toString());

        when(orderService.updateOrderStatus(any(UpdateOrderRequest.class))).thenReturn(response);

        String requestBody = """
        {
        "userId": 10,
        "order": {
                "id": 1,
                "status": "IN_PROGRESS"
        },
        "status": "DELIVERED"
        }
        """;

        mockMvc.perform(post("/api/orders/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.orderId").value(orderId))
        .andExpect(jsonPath("$.status").value("DELIVERED"))
        .andExpect(jsonPath("$.items.length()").value(2))
        .andExpect(jsonPath("$.items[0].itemName").value("Latte"))
        .andExpect(jsonPath("$.items[0].quantity").value(2));;
    }

    @Test
    @DisplayName("Should return only user's own orders when user is CUSTOMER")
    public void shouldReturnCustomerOrdersOnly() throws Exception {
        Long userId = 1L;

        OrderResponse response = new OrderResponse();
        response.setOrderId(101L);
        response.setStatus(OrderStatus.IN_PROGRESS.name());
        response.setItems(List.of(
            new OrderItemResponse("Latte", 1)
        ));

        when(orderService.getOrdersForUser(userId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders")
            .param("userId", String.valueOf(userId))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].orderId").value(101))
        .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Should return all orders when user is RESTAURANT")
    public void shouldReturnAllOrdersForRestaurantUser() throws Exception {
        Long userId = 2L;

        OrderResponse response1 = new OrderResponse();
        response1.setOrderId(201L);
        response1.setStatus(OrderStatus.APPROVED.name());
        response1.setItems(List.of());

        OrderResponse response2 = new OrderResponse();
        response2.setOrderId(202L);
        response2.setStatus(OrderStatus.DELIVERED.name());
        response2.setItems(List.of());

        when(orderService.getOrdersForUser(userId)).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/orders")
            .param("userId", String.valueOf(userId))
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(2))
          .andExpect(jsonPath("$[0].orderId").value(201))
          .andExpect(jsonPath("$[1].orderId").value(202));
    }
}
