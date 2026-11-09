package com.luisalvarez.orderservice.event;

public record OrderCancelledEvent(String orderNumber, String email, String reason) {
}
