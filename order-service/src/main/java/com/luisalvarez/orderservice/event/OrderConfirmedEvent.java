package com.luisalvarez.orderservice.event;

import lombok.Builder;

@Builder
public record OrderConfirmedEvent(String orderNumber, String email) {
}
