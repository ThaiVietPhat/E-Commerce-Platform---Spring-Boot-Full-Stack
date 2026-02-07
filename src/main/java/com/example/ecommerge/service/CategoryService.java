package com.example.ecommerge.service;

import com.example.ecommerge.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category savedNewCategory(Category category);
    List<Category> listCategories();
    void deleteCategory(UUID categoryId);
    void updateCategory(UUID categoryId, Category category);
}
