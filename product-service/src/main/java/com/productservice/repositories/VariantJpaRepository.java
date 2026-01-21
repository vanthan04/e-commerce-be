package com.productservice.repositories;

import com.productservice.models.Product;
import com.productservice.models.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VariantJpaRepository extends JpaRepository<Variant, UUID> {
    List<Variant> findAllByProduct(Product product);
}
