package com.luisalvarez.notificationservice.listener;

import com.luisalvarez.notificationservice.event.OrderCancelledEvent;
import com.luisalvarez.notificationservice.event.OrderConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@RabbitListener(queues = "notification-queue")
public class OrderEventsListener {

    private final JavaMailSender mailSender;

    @RabbitHandler
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {

        log.info("Order confirmed, order number: {}", event.orderNumber());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.email());
        message.setSubject("Order confirmed- " + event.orderNumber());
        message.setText("Esta es una prueba de email");

        mailSender.send(message);

        log.info("Sending email to: {}", event.email());
        log.info("Email sent to the order number: {}", event.orderNumber());
    }

    @RabbitHandler
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {

        log.info("Order cancelled, order number: {}", event.orderNumber());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.email());
        message.setSubject("Order cancelled - " + event.orderNumber());
        message.setText("This is a test");

        mailSender.send(message);

        log.info("Sending email to: {}", event.email());
        log.info("Email sent to the order number: {}", event.orderNumber());
    }
}
