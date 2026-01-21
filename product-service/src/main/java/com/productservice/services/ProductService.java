package com.productservice.services;

import com.productservice.dto.response.ProductDTORes;
import com.productservice.exception.AppException;
import com.productservice.exception.ErrorCode;
import com.productservice.models.Category;
import com.productservice.models.Product;
import com.productservice.repositories.CategoryJpaRepository;
import com.productservice.repositories.ProductJpaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductService {
    private final CloudinaryService cloudinaryService;
    private final CategoryJpaRepository categoryRepository;
    private final ProductJpaRepository productRepository;

    public UUID createProduct(
            String name,
            UUID categoryId,
            String description,
            Map<String, Object> productAttributes
    ) {

        // Kiểm tra loại sản phẩm tồn tại
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Kiểm tra trùng tên sản phẩm
        productRepository.findByName(name).ifPresent(p -> {
            throw new AppException(ErrorCode.PRODUCT_NAME_ALREADY_EXISTS);
        });

        try {
            // Tạo sản phẩm mới
            Product newProduct = new Product(
                    name,
                    description,
                    productAttributes,
                    category
            );
            // Lưu vào DB
            productRepository.save(newProduct);

            return newProduct.getProductId();

        } catch (Exception e) {
            System.out.println("Lỗi khi tạo sản phẩm" + e.getMessage());
            throw new AppException(ErrorCode.PRODUCT_CREATION_FAILED);
        }
    }


    public List<ProductDTORes> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            return new ProductDTORes(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getProductAttributes(),
                    product.isActive(),
                    product.getCategory(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            );
        }).toList();
    }

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public Product changeActive(UUID productId){
        Product product = getProductById(productId);
        product.changeActive();
        return productRepository.save(product);

    }
    public Product updateProduct(
            UUID productId,
            UUID categoryId,
            String name,
            String description,
            Map<String, Object> productAttributes
    ) {
        Product product = getProductById(productId);

        // Tìm ProductType theo typeId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        try {
            product.updateProduct(category, name, description, productAttributes);
            return productRepository.save(product);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật sản phẩm"+ e.getMessage());
            throw new AppException(ErrorCode.PRODUCT_UPDATE_FAILED);
        }
    }



    public void deleteProduct(UUID productId) {
        try {
            Optional<Product> existed = productRepository.findById(productId);
            if (existed.isEmpty()){
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            Product delProduct = existed.get();

            productRepository.delete(delProduct);

        } catch (Exception e){
            System.out.println("Lỗi hệ thống" + e.getMessage());
            throw new AppException(ErrorCode.PRODUCT_INTERNAL_SERVER_ERROR);
        }
    }



}
