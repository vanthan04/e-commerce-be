package com.productservice.controllers;

import com.productservice.dto.ApiResponse;
import com.productservice.dto.EnumCode;
import com.productservice.dto.response.CategoryRes;
import com.productservice.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping()
    public ApiResponse<UUID> createNewCategory(@RequestBody String name){
        return new ApiResponse<>(EnumCode.CATEGORY_CREATED, categoryService.createNewCategory(name));
    }

    @GetMapping()
    public ApiResponse<List<CategoryRes>> getAllCategories(){
        return new ApiResponse<>(EnumCode.CATEGORY_FETCHED, categoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryRes> getCategoryById(@PathVariable UUID id){
        return new ApiResponse<>(EnumCode.CATEGORY_FETCHED, categoryService.getCategoryById(id)) ;
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryRes> updateNameCategory(@PathVariable UUID id, @RequestBody String newName){
        return new ApiResponse<>(EnumCode.CATEGORY_UPDATED, categoryService.updateCategory(id, newName));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<UUID> deleteCategory(@PathVariable UUID id){
        return new ApiResponse<>(EnumCode.CATEGORY_DELETED, categoryService.deleteCategory(id));
    }
}
