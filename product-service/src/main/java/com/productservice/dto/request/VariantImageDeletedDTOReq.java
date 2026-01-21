package com.productservice.dto.request;

import java.util.List;

public record VariantImageDeletedDTOReq(
        List<String> imgUrls
) {
}
