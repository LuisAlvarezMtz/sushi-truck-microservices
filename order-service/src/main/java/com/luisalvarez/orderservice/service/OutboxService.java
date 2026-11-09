package com.luisalvarez.orderservice.service;

import com.luisalvarez.orderservice.event.OrderPlacedEvent;
import com.luisalvarez.orderservice.model.OutboxEvent;

import java.util.List;

public interface OutboxService {
    void saveOrderPlacedEvent(OrderPlacedEvent event, boolean isProcessed);
    List<OutboxEvent> getPendingEvent();
    void markAsProcessed(Long id);
}
