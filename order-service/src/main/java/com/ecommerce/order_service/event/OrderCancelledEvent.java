package com.ecommerce.order_service.event;

import lombok.Builder;

@Builder
public record OrderCancelledEvent(String orderNumber, String email, String reason) {
}
