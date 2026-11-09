package com.luisalvarez.orderservice.service.impl;

import com.luisalvarez.orderservice.dto.OrderRequest;
import com.luisalvarez.orderservice.dto.OrderResponse;
import com.luisalvarez.orderservice.event.OrderPlacedEvent;
import com.luisalvarez.orderservice.exception.ResourceNotFoundException;
import com.luisalvarez.orderservice.mapper.OrderMapper;
import com.luisalvarez.orderservice.model.Order;
import com.luisalvarez.orderservice.model.OrderStatus;
import com.luisalvarez.orderservice.repository.OrderRepository;
import com.luisalvarez.orderservice.service.OrderService;
import com.luisalvarez.orderservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@RefreshScope
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OutboxService outboxService;

//    private final InventoryClient inventoryClient;
    @Value("${order.enabled:true}")
    private boolean ordersEnabled;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest, String userId) {
        if(!ordersEnabled){
            log.warn("Order rejected: unable service to config");
            throw  new RuntimeException("Orders Service is in maintenance now. Try later");
        }

        log.info("Placing new order");

        Order order = orderMapper.toOrder(orderRequest);
        order.setUserId(userId);
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PLACED);
        Order savedOrder = orderRepository.save(order);

        log.info(
                "Order successfully placed with id: {} and items: {}",
                savedOrder.getId(),
                savedOrder.getOrderLineItemsList().size()
        );

        List<OrderPlacedEvent.OrderItemEvent> orderItems = order.getOrderLineItemsList().stream()
                .map(item -> new OrderPlacedEvent.OrderItemEvent(
                        item.getSku(),
                        item.getPrice().toString(),
                        item.getQuantity()
                )).toList();

        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getOrderNumber(), orderRequest.email(), orderItems
        );

        boolean sentToRabbit = false;
        try {
            rabbitTemplate.convertAndSend("order-events", "order.placed", event);
            sentToRabbit = true;
            log.info("Message sent immediately to RabbitMQ: {}", savedOrder.getOrderNumber());
        } catch (AmqpException e) {
            log.error("RabbitMQ fall. Outbox save your order: {}", savedOrder.getOrderNumber());
        }

        outboxService.saveOrderPlacedEvent(event, sentToRabbit);

        log.info("Order sent to rabbit mq, ordern number: {}", savedOrder.getOrderNumber());

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String userId, boolean isAdmin) {
        List<Order> orders;

        if(isAdmin){
            orders = orderRepository.findAll();
        }else{
            orders = orderRepository.findByUserId(userId);
        }

        return orders.stream().map(orderMapper::toOrderResponse)
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

    @Override
    @Transactional
    public void updateOrderStatus(String orderNumber, OrderStatus newStatus) {

        orderRepository.findByOrderNumber(orderNumber).ifPresentOrElse(
                order -> {
                    order.setStatus(newStatus);
                    orderRepository.save(order);
                    log.info("Status updated to order number: {}", orderNumber);
                },
                () -> log.error("Order number: {} not found", orderNumber)
        );
    }
}
