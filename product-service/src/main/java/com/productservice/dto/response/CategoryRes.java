package com.productservice.dto.response;

import java.util.UUID;

public record CategoryRes(
        UUID categoryId,
        String name
) {
}
