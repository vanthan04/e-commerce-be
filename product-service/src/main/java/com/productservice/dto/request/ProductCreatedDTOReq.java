package com.productservice.dto.request;


import java.util.Map;
import java.util.UUID;

public record ProductCreatedDTOReq(
        String name,
        UUID categoryId,
        String description,
        Map<String, Object> productAttributes
) {}
