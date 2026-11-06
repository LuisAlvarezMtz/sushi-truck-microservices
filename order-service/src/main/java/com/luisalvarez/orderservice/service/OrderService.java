package com.luisalvarez.orderservice.service;

import com.luisalvarez.orderservice.dto.OrderRequest;
import com.luisalvarez.orderservice.dto.OrderResponse;
import com.luisalvarez.orderservice.model.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest, String useId);
    List<OrderResponse> getOrders(String userId, boolean isAdmin);
    OrderResponse getOrderById(Long id);
    void deleteOrder(Long id);
    void updateOrderStatus(String orderNumber, OrderStatus newStatus);
}
