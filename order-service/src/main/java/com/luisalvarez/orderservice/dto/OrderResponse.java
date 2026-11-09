package com.luisalvarez.orderservice.dto;

import com.luisalvarez.orderservice.model.OrderStatus;

import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        OrderStatus status,
        List<OrderLineItemsResponse> orderLineItemsList
) { }
