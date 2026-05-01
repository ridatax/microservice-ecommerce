package com.ecommerce.order_service.event;

import java.util.List;
import lombok.Builder;

@Builder
public record OrderPlacedEvent(String orderNumber, String email, List<OrderItemEvent> items) {

    @Builder
    public record OrderItemEvent(String sku, String price, Integer quantity){}
}
