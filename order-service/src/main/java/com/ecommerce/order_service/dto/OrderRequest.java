package com.ecommerce.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotEmpty(message = "order must contain at least one item")
    @Valid
    private List<OrderLineItemsRequest> orderLineItemsList;

    @NotBlank(message = "email is required")
    @Email(message = "email is not valid")
    private String email;
}
