package com.luisalvarez.orderservice.service.impl;

import com.luisalvarez.orderservice.dto.OrderRequest;
import com.luisalvarez.orderservice.dto.OrderResponse;
import com.luisalvarez.orderservice.exception.ResourceNotFoundException;
import com.luisalvarez.orderservice.mapper.OrderMapper;
import com.luisalvarez.orderservice.model.Order;
import com.luisalvarez.orderservice.repository.OrderRepository;
import com.luisalvarez.orderservice.service.OrderService;
import com.luisalvarez.orderservice.service.client.InventoryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryClient inventoryClient;

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        log.info("Placing new order");

        Order order = orderMapper.toOrder(orderRequest);

        for(var item: order.getOrderLineItemsList()){
            String sku = item.getSku();
            Integer quantity = item.getQuantity();

            try {
                inventoryClient.reduceStock(sku, quantity);
            } catch (Exception e) {
                log.error("Error to reduce stock to product {}: {}", sku, e.getMessage());
                throw new IllegalArgumentException("Could not process the order");
            }
        }

        order.setOrderNumber(UUID.randomUUID().toString());

        Order savedOrder = orderRepository.save(order);

        log.info(
                "Order successfully placed with id: {} and items: {}",
                savedOrder.getId(),
                savedOrder.getOrderLineItemsList().size()
        );

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order", "id", id));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order", "id", id));

        orderRepository.delete(order);
    }
}
