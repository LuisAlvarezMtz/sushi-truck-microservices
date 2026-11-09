package com.luisalvarez.orderservice.scheduler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luisalvarez.orderservice.event.OrderPlacedEvent;
import com.luisalvarez.orderservice.model.OutboxEvent;
import com.luisalvarez.orderservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageRelayer {

    private final RabbitTemplate rabbitTemplate;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 10000)
    public void relayMessage() {

        List<OutboxEvent> pendingEvents = outboxService.getPendingEvent();

        if (!pendingEvents.isEmpty()) {
            log.info("Relayer: Detects {} pending messages", pendingEvents.size());

            for (OutboxEvent e : pendingEvents) {
                try {
                    OrderPlacedEvent originalEvent = objectMapper.readValue(
                            e.getPayload(), OrderPlacedEvent.class);

                    rabbitTemplate.convertAndSend("order-events", "order.placed", originalEvent);
                    outboxService.markAsProcessed(e.getId());
                } catch (JacksonException ex){
                    log.error("Error to serialize event {}: {}", e.getId(), ex.getMessage());
                } catch (AmqpException ex){
                    log.error("Attempt failed to event {}: {}", e.getId(), ex.getMessage());
                    throw new RuntimeException(ex);
                }


            }
        }


    }

}
