package com.productservice.exception;

public enum ErrorCode {

    CATEGORY_NOT_FOUND(1450, "Category not found"),
    CATEGORY_ALREADY_EXISTS(1451, "Category name already exists"),
    CATEGORY_NOT_EMPTY(1452, "Category is not empty"),
    CATEGORY_NAME_INVALID(1453, "Category name is invalid"),

    PRODUCT_IMAGE_UPLOAD_FAILED(1460, "Failed to upload product image"),
    PRODUCT_IMAGE_DELETE_FAILED(1461, "Failed to delete product image"),
    PRODUCT_CREATION_FAILED(1462, "Failed to create product"),
    PRODUCT_NOT_FOUND(1463, "Product not found"),
    PRODUCT_UPDATE_FAILED(1464, "Failed to update product"),
    PRODUCT_NAME_ALREADY_EXISTS(1465, "Product name already exists"),
    PRODUCT_IMAGE_PROCESSING_ERROR(1466, "Error while processing product image"),
    PRODUCT_PRICE_INVALID(1467, "Price must be greater than 0"),
    PRODUCT_NOT_ACTIVE(1468, "Product is not active"),
    VARIANT_NOT_FOUND(1469, "Variant not found"),
    PRODUCT_ACTIVE_NOT_CHANGE(1470, "product active is not change"),


    PRODUCT_INTERNAL_SERVER_ERROR(1499, "Internal server error");

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage(){
        return message;
    }
}
