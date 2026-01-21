package com.productservice.services;

import com.productservice.dto.response.VariantDTORes;
import com.productservice.exception.AppException;
import com.productservice.exception.ErrorCode;
import com.productservice.messaging.producer.inventory.ProductCreatedEvent;
import com.productservice.models.Product;
import com.productservice.models.ProductImage;
import com.productservice.models.Variant;
import com.productservice.repositories.VariantJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@AllArgsConstructor
@Service
public class VariantService {
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final VariantJpaRepository variantRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductService productService;

    public UUID createNewVariant(
        UUID productId,
        Map<String, Object> variantAttributes,
        BigDecimal price,
        MultipartFile[] files
    ){
        Product product = productService.getProductById(productId);
        if (product.isActiveProduct()){
            throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE);
        }

        List<String> imgUrls = cloudinaryService.uploadFiles(files, product.getName());

        Variant variant = new Variant(
                product,
                variantAttributes,
                price,
                new ArrayList<>()
        );

        imgUrls.forEach(url -> {
            ProductImage image = new ProductImage(url);
            variant.addImage(image);
        });

        kafkaTemplate.send("productCreated", new ProductCreatedEvent(productId, variant.getId()));

        Variant savedVariant = variantRepository.save(variant);

        return savedVariant.getId();

    }

    public Variant updateVariant(
            UUID variantId,
            Map<String, Object> variantAttribute,
            BigDecimal price,
            List<String> keepUrls,
            MultipartFile[] files
    ){
        Variant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));

        List<String> listUrlImg = variant.getImageList().stream().map(ProductImage::getImageUrl).toList();
        return null;
    }


    public VariantDTORes getAllVariantByProductId(UUID productId){
        Product product = productService.getProductById(productId);
        if (product.isActiveProduct()){
            throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE);
        }

        return new VariantDTORes(
                productId,
                variantRepository.findAllByProduct(product)
        );
    }

    public void deleteImage(UUID variantId, List<String> urlsToDelete) {
        Variant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));

        List<ProductImage> updatedImages = variant.getImageList().stream()
                .filter(img -> !urlsToDelete.contains(img.getImageUrl()))
                .toList();

        variant.setImageList(updatedImages);
        variantRepository.save(variant);
    }

    public UUID deleteVariantById(UUID variantId){
        try {
            Optional<Variant> existed = variantRepository.findById(variantId);
            if (existed.isEmpty()){
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            Variant delProduct = existed.get();

            variantRepository.delete(delProduct);
            return delProduct.getId();

        } catch (Exception e){
            System.out.println("Lỗi hệ thống" + e.getMessage());
            throw new AppException(ErrorCode.PRODUCT_INTERNAL_SERVER_ERROR);
        }
    }



}
