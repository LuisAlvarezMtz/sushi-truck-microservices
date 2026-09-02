package com.luisalvarez.productservice.dto;

import com.luisalvarez.productservice.model.ProductCategory;

import java.math.BigDecimal;

public record ProductResponseDto(
        String id,
        String name,
        String description,
        ProductCategory category,
        BigDecimal price
) {}
