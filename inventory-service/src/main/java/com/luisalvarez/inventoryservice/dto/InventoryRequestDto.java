package com.luisalvarez.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record InventoryRequestDto(
    @NotBlank(message = "Sku cannot be empty")
    String sku,
    @Min(value = 0, message = "Quantity cannot be negative")
    Integer quantityAvailable,
    @Positive(message = "Low stock threshold must be greater than 0")
    Integer lowStockThreshold
){}
