package com.luisalvarez.orderservice.dto;

import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        List<OrderLineItemsResponse> orderLineItemsList
) { }
