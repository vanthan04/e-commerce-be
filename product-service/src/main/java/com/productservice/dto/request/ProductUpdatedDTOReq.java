package com.productservice.dto.request;

import java.util.Map;
import java.util.UUID;

public record ProductUpdatedDTOReq(
        UUID categoryId,
        String productName,
        String productDescription,
        Map<String, Object> productAttributes
) {
}
