package com.luisalvarez.orderservice.service;

import com.luisalvarez.orderservice.dto.OrderRequest;
import com.luisalvarez.orderservice.dto.OrderResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OrderResponse> placeOrder(OrderRequest orderRequest, String useId);
    List<OrderResponse> getOrders(String userId, boolean isAdmin);
    OrderResponse getOrderById(Long id);
    void deleteOrder(Long id);
}
