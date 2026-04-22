package com.ecommerce.product_service.dto;

public record ProductResponseDto(
        String id,
        String name,
        String description,
        double price
) {
}
