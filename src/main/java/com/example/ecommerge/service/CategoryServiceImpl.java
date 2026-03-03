package com.example.ecommerge.service;

import com.example.ecommerge.mappers.CategoryMapper;
import com.example.ecommerge.model.CategoryDTO;
import com.example.ecommerge.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public CategoryDTO savedNewCategory(CategoryDTO categoryDTO) {
        return categoryMapper.categoryToCategoryDTO(categoryRepository.save(categoryMapper.categoryDTOToCategory(categoryDTO)));
    }

    @Override
    public List<CategoryDTO> listCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::categoryToCategoryDTO).toList();
    }

    @Override
    public boolean deleteCategory(UUID categoryId) {
        if(categoryRepository.existsById(categoryId)){
            categoryRepository.deleteById(categoryId);
            return true;
        }return false;
    }

    @Override
    public Optional<CategoryDTO> updateCategory(UUID categoryId, CategoryDTO categoryDTO) {
        return categoryRepository.findById(categoryId).map(foundCategory -> {
            foundCategory.setCategoryName(categoryDTO.getCategoryName());
            return categoryMapper.categoryToCategoryDTO(categoryRepository.save(foundCategory));
        });
    }
}
