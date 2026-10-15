package com.luisalvarez.notificationservice.event;

import lombok.Builder;

@Builder
public record OrderConfirmedEvent(String orderNumber, String email) {
}
