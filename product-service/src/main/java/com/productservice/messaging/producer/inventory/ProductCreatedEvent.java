package com.productservice.messaging.producer.inventory;


import java.util.UUID;

public record ProductCreatedEvent(
        UUID productId,
        UUID variantId
) {
}
