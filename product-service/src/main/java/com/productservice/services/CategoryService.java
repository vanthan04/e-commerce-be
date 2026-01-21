package com.productservice.services;

import com.productservice.dto.response.CategoryRes;
import com.productservice.exception.AppException;
import com.productservice.exception.ErrorCode;
import com.productservice.models.Category;
import com.productservice.repositories.CategoryJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryJpaRepository categoryRepository;

    public List<CategoryRes> getAllCategory() {
        List<Category> entities = categoryRepository.findAll();
        return entities.stream()
                .map(pt -> new CategoryRes(pt.getId(), pt.getName()))
                .toList();
    }

    public UUID createNewCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NAME_INVALID);
        }

        // Check tên đã tồn tại chưa
        boolean exists = categoryRepository.existsByNameIgnoreCase(name.trim());
        if (exists) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = new Category(name.trim());
        categoryRepository.save(category);
        return  category.getId();
    }

    public CategoryRes getCategoryById(UUID id){
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return new CategoryRes(existing.getId(), existing.getName());
    }

    public CategoryRes updateCategory(UUID id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NAME_INVALID);
        }

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        existing.setName(newName.trim());
        categoryRepository.save(existing);
        return new CategoryRes(existing.getId(), existing.getName());
    }

    public UUID deleteCategory(UUID id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(existing);
        return id;
    }
}
