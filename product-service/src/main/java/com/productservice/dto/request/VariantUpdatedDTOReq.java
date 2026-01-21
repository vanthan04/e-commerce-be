package com.productservice.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record VariantUpdatedDTOReq(
        Map<String, Object> variantAttributes,
        BigDecimal price,
        List<String> keepUrls,
        MultipartFile[] files
) {
}
