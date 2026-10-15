package com.luisalvarez.inventoryservice.listener;

import com.luisalvarez.inventoryservice.event.OrderPlacedEvent;
import com.luisalvarez.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventsListener {

    private final InventoryService inventoryService;

    @RabbitListener(queues = "inventory-queue")
    public void handleOrderPlacedEvent(OrderPlacedEvent event){
        event.items().forEach(item -> {

            try {
                inventoryService.reduceStock(item.sku(), item.quantity());
                log.info("Stock reduced for sku: {} - quantity: {}", item.sku(), item.quantity());
            }catch (Exception e){
                log.error("Error to reduce stock for sku: {}", item.sku());
            }

        });
    }
}
