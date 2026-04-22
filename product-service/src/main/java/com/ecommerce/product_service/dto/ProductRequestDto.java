package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDto(
        @NotBlank(message = "Product name cannot be blank")
        String name,
        String description,
        @NotNull(message = "Product price cannot be null")
        @Positive(message = "Product price must be positive")
        double price
) {
}
