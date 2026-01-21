package com.productservice.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.productservice.exception.AppException;
import com.productservice.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variant {
    @Id
    private UUID id;

    @Column(columnDefinition = "JSONB")
    @JdbcTypeCode(Types.OTHER)
    private Map<String, Object> variantAttributes;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "variant", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductImage> imageList = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Variant(
            Product product,
            Map<String, Object> variantAttributes,
            BigDecimal price,
            List<ProductImage> productImages
    ){
        this.id = UUID.randomUUID();
        this.product = product;
        this.product.getVariantList().add(this);

        this.variantAttributes = variantAttributes;
        if (price.compareTo(BigDecimal.ZERO) <= 0){
            throw new AppException(ErrorCode.PRODUCT_PRICE_INVALID);
        }
        this.price = price;
        this.imageList = productImages;

        productImages.forEach(this::addImage);
    }

    public void updateVariant(
            UUID variantId,
            Map<String, Object> variantAttribute,
            BigDecimal price,
            boolean isActive,
            List<String> keepUrls,
            MultipartFile[] files
    ){

    }
    public void addImage(ProductImage image) {
        this.imageList.add(image);
        image.setVariant(this);
    }

    public void removeImage(ProductImage image) {
        this.imageList.remove(image);
        image.setVariant(null);
    }

}
