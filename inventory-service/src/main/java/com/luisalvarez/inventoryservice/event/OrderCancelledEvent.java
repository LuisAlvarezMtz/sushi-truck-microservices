package com.luisalvarez.inventoryservice.event;

public record OrderCancelledEvent(String orderNumber, String email, String reason) {
}
