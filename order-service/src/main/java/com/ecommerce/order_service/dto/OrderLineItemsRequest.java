package com.ecommerce.order_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsRequest {

    @NotBlank(message = "sku can't be blank")
    private String sku;

    @NotNull(message = "price can't be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "price can't be negative")
    private BigDecimal price;

    @NotNull(message = "quantity can't be null")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;
}
