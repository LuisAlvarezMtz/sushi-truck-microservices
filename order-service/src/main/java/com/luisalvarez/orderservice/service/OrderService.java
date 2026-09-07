package com.luisalvarez.orderservice.service;

import com.luisalvarez.orderservice.dto.OrderRequest;
import com.luisalvarez.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
    void deleteOrder(Long id);
}
