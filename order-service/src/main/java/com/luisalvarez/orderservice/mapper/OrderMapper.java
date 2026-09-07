package com.luisalvarez.orderservice.mapper;

import com.luisalvarez.orderservice.dto.OrderLineItemsRequest;
import com.luisalvarez.orderservice.dto.OrderLineItemsResponse;
import com.luisalvarez.orderservice.dto.OrderRequest;
import com.luisalvarez.orderservice.dto.OrderResponse;
import com.luisalvarez.orderservice.model.Order;
import com.luisalvarez.orderservice.model.OrderLineItems;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Request to Entity
    Order toOrder(OrderRequest orderRequest);

    //Request to Entity
    OrderLineItems toOrderLineItems(OrderLineItemsRequest orderLineItemsRequest);

    // Entidad to Response
    OrderResponse toOrderResponse(Order order);

    // Aux method to response
    OrderLineItemsResponse toOrderLineItemsResponse(OrderLineItems orderLineItems);
}