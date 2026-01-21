package com.productservice.dto;

public enum EnumCode {

    // --- Category Codes ---
    CATEGORY_FETCHED(1400, "Category fetched successfully"),
    CATEGORY_CREATED(1401, "Category created successfully"),
    CATEGORY_UPDATED(1402, "Category updated successfully"),
    CATEGORY_DELETED(1403, "Category deleted successfully"),

    // --- Product Codes ---
    PRODUCT_CREATED(1404, "Product created successfully"),
    PRODUCT_UPDATED(1405, "Product updated successfully"),
    PRODUCT_DELETED(1406, "Product deleted successfully"),
    PRODUCT_FETCHED(1407, "Product fetched successfully"),
    PRODUCT_CHANGE_ACTIVE(1408, "Change active product"),

    VARIANT_CREATED(1409, "Variant created successfully"),
    VARIANT_UPDATED(1410, "Variant updated successfully"),
    VARIANT_FETCHED(1411, "Variant fetched successfully"),
    VARIANT_DELETED(1412, "Variant deleted successfully"),
    VARIANT_DELETED_IMAGE_SUCCESSFULLY(1413, "Delete variant image successfully"),
    VARIANT_DELETED_IMAGE_FAILED(1414, "Delete variant image failed");

    private final int statusCode;
    private final String message;

    EnumCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
