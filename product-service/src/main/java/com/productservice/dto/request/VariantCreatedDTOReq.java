package com.productservice.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record VariantCreatedDTOReq (
        UUID productId,
        Map<String, Object> attributes,
        BigDecimal price,
        MultipartFile[] images
){
    public VariantCreatedDTOReq {
        if (productId == null) {
            throw new IllegalArgumentException("productId không được null");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price phải >= 0");
        }
        if (images == null || images.length == 0) {
            throw new IllegalArgumentException("Phải có ít nhất 1 ảnh");
        }
    }
}
