package com.luisalvarez.orderservice.service.impl;

import com.luisalvarez.orderservice.event.OrderPlacedEvent;
import com.luisalvarez.orderservice.model.OutboxEvent;
import com.luisalvarez.orderservice.repository.OutboxRepository;
import com.luisalvarez.orderservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxServiceImplement implements OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void saveOrderPlacedEvent(OrderPlacedEvent event, boolean isProcessed) {

        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateId(event.orderNumber())
                .type("ORDER_PLACED")
                .payload(payload)
                .createdAt(LocalDateTime.now())
                .processed(isProcessed)
                .build();

        outboxRepository.save(outboxEvent);
        log.info("Event safe in outbox: {}", event.orderNumber());


    }

    @Override
    public List<OutboxEvent> getPendingEvent() {
        return outboxRepository.findByProcessedFalse();
    }

    @Override
    public void markAsProcessed(Long id) {
        outboxRepository.findById(id).ifPresent(event -> {
            event.setProcessed(true);
            outboxRepository.save(event);
            log.info("Event {} mark as processed", id );
        });
    }
}
