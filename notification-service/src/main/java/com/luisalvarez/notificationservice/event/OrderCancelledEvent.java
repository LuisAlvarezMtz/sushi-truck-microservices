package com.luisalvarez.notificationservice.event;

public record OrderCancelledEvent(String orderNumber, String email, String reason) {
}
