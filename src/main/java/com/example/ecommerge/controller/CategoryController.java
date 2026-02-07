package com.example.ecommerge.controller;

import com.example.ecommerge.model.Category;
import com.example.ecommerge.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private static final String CATEGORY_PATH = "/api/public/category";
    private static final String CATEGORY_PATH_ID =  CATEGORY_PATH+"/{categoryId}";
    private final CategoryService categoryService;
    @PutMapping(CATEGORY_PATH_ID)
    public ResponseEntity<Category> updateCategory(@PathVariable UUID categoryId, @RequestBody Category category){
        categoryService.updateCategory(categoryId,category);
        return ResponseEntity.noContent().build();
    }
    @GetMapping(CATEGORY_PATH)
    public List<Category> getAllCategories()
    {
        return categoryService.listCategories();
    }
    @PostMapping(CATEGORY_PATH)
    public ResponseEntity<Category> handlePost(@RequestBody Category category)
    {
        Category savedCategory = categoryService.savedNewCategory(category);
        return ResponseEntity.created(URI.create(CATEGORY_PATH + "/" + savedCategory.getCategoryId().toString()))
                .build();
    }
    @DeleteMapping(CATEGORY_PATH_ID)
    public ResponseEntity<Category> deleteCategoryById(@PathVariable UUID categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
