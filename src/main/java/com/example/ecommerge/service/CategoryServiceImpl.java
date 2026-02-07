package com.example.ecommerge.service;

import com.example.ecommerge.model.Category;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService{


    private Map<UUID, Category> categoryMap;
    public CategoryServiceImpl() {
        categoryMap = new HashMap<>();
    }
    @Override
    public List<Category> listCategories() {
        return new ArrayList<>(categoryMap.values());
    }

    @Override
    public void deleteCategory(UUID categoryId) {
        categoryMap.remove(categoryId);
    }

    @Override
    public Category savedNewCategory(Category category) {
        Category savedCategory=Category.builder()
                .categoryId(UUID.randomUUID())
                .categoryName(category.getCategoryName())
                .build();
        categoryMap.put(savedCategory.getCategoryId(),savedCategory);
        return savedCategory;
    }
    @Override
    public void updateCategory(UUID categoryId, Category category) {
        Category existing = categoryMap.get(categoryId);
        existing.setCategoryName(category.getCategoryName());
    }

}
