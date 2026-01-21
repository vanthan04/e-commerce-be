package com.productservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "JSONB")
    @JdbcTypeCode(Types.OTHER)
    private Map<String, Object> productAttributes;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Variant> variantList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Product(
            String name,
            String description,
            Map<String, Object> productAttributes,
            Category category
    ){
        this.productId = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.productAttributes = productAttributes;
        this.category = category;
        this.category.getProductList().add(this);
    }

    public void updateProduct(
            Category category,
            String name,
            String description,
            Map<String, Object> productAttributes
    ){
        if (this.category != null) {
            this.category.getProductList().remove(this);
        }
        this.category = category;
        if (category != null) {
            category.getProductList().add(this);
        }
        this.name = name;
        this.description = description;
        this.productAttributes = productAttributes;
    }

    public boolean isActiveProduct(){
        return !this.isActive;
    }


    public void changeActive() {
        this.isActive = !isActive;
    }
}

