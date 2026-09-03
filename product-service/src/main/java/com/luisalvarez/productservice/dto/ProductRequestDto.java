package com.luisalvarez.productservice.dto;

import com.luisalvarez.productservice.model.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "Product name cannot be empty")
        String name,
        String description,
        @NotNull(message = "Category can´t be empty")
        ProductCategory category,
        @NotNull(message = "Price is mandatory")
        @Positive(message = "Price must be greater than 0")
        BigDecimal price
) { }
