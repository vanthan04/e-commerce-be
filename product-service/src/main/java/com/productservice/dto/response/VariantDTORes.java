package com.productservice.dto.response;

import com.productservice.models.Variant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record VariantDTORes(
        UUID productId,
        List<Variant> variants
) {
}
