package com.productservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productservice.dto.ApiResponse;
import com.productservice.dto.EnumCode;
import com.productservice.dto.request.ProductCreatedDTOReq;
import com.productservice.dto.request.ProductUpdatedDTOReq;
import com.productservice.dto.response.ProductDTORes;
import com.productservice.services.ProductService;
import com.productservice.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ApiResponse<UUID> createNewProduct(@ModelAttribute ProductCreatedDTOReq request) {

        // gọi service
        UUID saved = productService.createProduct(
                request.name(),
                request.categoryId(),
                request.description(),
                request.productAttributes()
        );

        return new ApiResponse<>(EnumCode.PRODUCT_CREATED, saved);
    }

    @PutMapping("/{productId}/active")
    public ApiResponse<Product> changeActiveProduct(@PathVariable UUID productId){
        return new ApiResponse<>(EnumCode.PRODUCT_CHANGE_ACTIVE, productService.changeActive(productId));
    }
    @PutMapping("/{productId}")
    public ApiResponse<Product> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductUpdatedDTOReq req
    ) {
        Product updated = productService.updateProduct(
                productId,
                req.categoryId(),
                req.productName(),
                req.productDescription(),
                req.productAttributes()
        );
        return new ApiResponse<>(EnumCode.PRODUCT_UPDATED, updated);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return new ApiResponse<>(EnumCode.PRODUCT_DELETED, null);
    }

    @GetMapping("/{productId}")
    public ApiResponse<Product> getProductById(@PathVariable UUID productId) {
        Product product = productService.getProductById(productId);
        return new ApiResponse<>(EnumCode.PRODUCT_FETCHED, product);
    }

    @GetMapping
    public ApiResponse<List<ProductDTORes>> getAllProducts() {
        List<ProductDTORes> products = productService.getAllProducts();
        return new ApiResponse<>(EnumCode.PRODUCT_FETCHED, products);
    }



}
