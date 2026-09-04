package com.luisalvarez.inventoryservice.dto;

public record InventoryResponseDto(
    Long id,
    String sku,
    Integer quantityAvailable,
    boolean inStock,
    Integer lowStockThreshold
){}
