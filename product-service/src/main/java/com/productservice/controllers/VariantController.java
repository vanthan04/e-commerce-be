package com.productservice.controllers;

import com.productservice.dto.ApiResponse;
import com.productservice.dto.EnumCode;
import com.productservice.dto.request.VariantCreatedDTOReq;
import com.productservice.dto.request.VariantImageDeletedDTOReq;
import com.productservice.dto.request.VariantUpdatedDTOReq;
import com.productservice.dto.response.VariantDTORes;
import com.productservice.models.Variant;
import com.productservice.services.ProductService;
import com.productservice.services.VariantService;
import lombok.AllArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products/variants")
public class VariantController {
    private final VariantService variantService;

    @PostMapping()
    public ApiResponse<UUID> createNewVariant(@ModelAttribute VariantCreatedDTOReq request){
        UUID variantId = variantService.createNewVariant(
                request.productId(),
                request.attributes(),
                request.price(),
                request.images()
        );
        return new ApiResponse<>(EnumCode.VARIANT_CREATED, variantId);
    }


    @GetMapping("/{productId}")
    public ApiResponse<VariantDTORes> getAllVariantByProductId(@PathVariable UUID productId){
        VariantDTORes variantRes = variantService.getAllVariantByProductId(productId);
        return new ApiResponse<>(EnumCode.VARIANT_FETCHED, variantRes);
    }

    @PutMapping("/{variantId}")
    public ApiResponse<Variant> updateVariant(@PathVariable UUID variantId, @ModelAttribute VariantUpdatedDTOReq request){
        Variant updateVariant = variantService.updateVariant(
                variantId,
                request.variantAttributes(),
                request.price(),
                request.keepUrls(),
                request.files()
        );
        return new ApiResponse<>(EnumCode.VARIANT_UPDATED, updateVariant);
    }

    @DeleteMapping("/{variantId}/delete-image")
    public ApiResponse<Void> deleteImage(@PathVariable UUID variantId, @RequestBody VariantImageDeletedDTOReq request){
        variantService.deleteImage(variantId, request.imgUrls());
        return new ApiResponse<>(EnumCode.VARIANT_DELETED_IMAGE_SUCCESSFULLY, null);
    }

    @DeleteMapping("/{variantId}")
    public ApiResponse<UUID> deleteVariant(@PathVariable UUID variantId){
        UUID id = variantService.deleteVariantById(variantId);
        return new ApiResponse<>(EnumCode.VARIANT_DELETED, id);
    }
}
