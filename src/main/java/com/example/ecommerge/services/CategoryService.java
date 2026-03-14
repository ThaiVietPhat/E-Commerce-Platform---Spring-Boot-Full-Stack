package com.example.ecommerge.services;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.models.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
public interface CategoryService {
    CategoryDTO saveNewCategory(CategoryDTO categoryDTO);      // ✅ sửa tên
    Page<CategoryDTO> pageCategories(Pageable pageable);
    void deleteCategory(UUID categoryId);                      // ✅ void
    void updateCategory(UUID categoryId, CategoryDTO categoryDTO); // ✅ void
    Optional<CategoryDTO> findById(UUID categoryId);
}
