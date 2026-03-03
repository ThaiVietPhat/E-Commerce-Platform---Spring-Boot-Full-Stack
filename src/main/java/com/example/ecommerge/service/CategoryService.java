package com.example.ecommerge.service;

import com.example.ecommerge.model.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface CategoryService {
    CategoryDTO savedNewCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> listCategories();
    boolean deleteCategory(UUID categoryId);
    Optional<CategoryDTO> updateCategory(UUID categoryId, CategoryDTO categoryDTO);
}
