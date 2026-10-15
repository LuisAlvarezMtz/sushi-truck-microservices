package com.luisalvarez.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(
        @NotEmpty(message = "La orden debe contener al menos un item")
        @Valid
        List<OrderLineItemsRequest> orderLineItemsList,

        @Email(message = "email format is not valid")
        @NotBlank(message = "email is requeried")
        String email
) {}
