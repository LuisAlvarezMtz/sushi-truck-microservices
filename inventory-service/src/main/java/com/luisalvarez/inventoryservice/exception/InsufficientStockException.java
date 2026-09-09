package com.luisalvarez.inventoryservice.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {
    private final String sku;
    private final Integer requestedQuantity;
    private final Integer availableQuantity;

    public InsufficientStockException(String sku, Integer requestedQuantity, Integer availableQuantity) {
        super(String.format(
                "Insufficient stock for sku '%s': requested %d, available %d",
                sku, requestedQuantity, availableQuantity
        ));
        this.sku = sku;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
}
