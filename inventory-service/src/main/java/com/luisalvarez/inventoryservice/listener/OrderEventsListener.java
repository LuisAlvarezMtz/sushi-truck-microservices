package com.luisalvarez.inventoryservice.listener;

import com.luisalvarez.inventoryservice.event.OrderCancelledEvent;
import com.luisalvarez.inventoryservice.event.OrderPlacedEvent;
import com.luisalvarez.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventsListener {

    private final InventoryService inventoryService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "inventory-queue")
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        try {

            boolean allProductsInStock = event.items().stream()
                    .allMatch(item -> inventoryService.isInStock(item.sku(), item.quantity()));

            if (!allProductsInStock) {
                cancelledOrder(event, "insufficient stock");
                return;
            }

            event.items().forEach(item -> {
                inventoryService.reduceStock(item.sku(), item.quantity());
            });

            rabbitTemplate.convertAndSend("order-events", "order.confirmed", event);

            log.info("Stock reduced for order number: {}", event.orderNumber());
        } catch (Exception e) {
            log.error("Error to reduce stock: {}", e.getMessage());
            cancelledOrder(event, "Inventory technical error");

        }

    }

    private void cancelledOrder(OrderPlacedEvent event, String reason) {
        OrderCancelledEvent cancelledEvent = new OrderCancelledEvent(
                event.orderNumber(), event.email(), reason
        );

        rabbitTemplate.convertAndSend("order-events", "order.cancelled", cancelledEvent);

    }
}
