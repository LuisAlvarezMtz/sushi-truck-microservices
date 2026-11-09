package com.luisalvarez.orderservice.listener;

import com.luisalvarez.orderservice.event.OrderCancelledEvent;
import com.luisalvarez.orderservice.event.OrderConfirmedEvent;
import com.luisalvarez.orderservice.model.OrderStatus;
import com.luisalvarez.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;

    @RabbitListener(queues = "order-confirmed-queue")
    public void handleOrderConfirmed(OrderConfirmedEvent event){
        if(event.orderNumber() == null){
            log.error("Event has not a correct order number");
            return;
        }
        orderService.updateOrderStatus(event.orderNumber(), OrderStatus.CONFIRMED);
    }

    @RabbitListener(queues = "order-cancelled-queue")
    public void handleOrderCancelled(OrderCancelledEvent event){
        if(event.orderNumber() == null){
            log.error("Event has not a correct order number");
            return;
        }
        orderService.updateOrderStatus(event.orderNumber(), OrderStatus.CANCELLED);
    }

}
