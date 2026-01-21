package com.orderservice.dto.request;


import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record OrderItemRequest(
        UUID productId,
        String productName,
        UUID variantId,
        Map<String, Object> attributes,
        int quantity,
        BigDecimal price
) {

}
