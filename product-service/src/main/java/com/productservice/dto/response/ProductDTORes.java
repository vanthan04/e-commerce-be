package com.productservice.dto.response;

import com.productservice.models.Category;
import com.productservice.models.Variant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductDTORes(
        UUID productId,
        String productName,
        String productDescription,
        Map<String, Object> productAttribute,
        boolean isActive,
        Category category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
}
