package com.example.ecommerge.services;
import com.example.ecommerge.entities.Category;
import com.example.ecommerge.exceptions.NotFoundException;
import com.example.ecommerge.mappers.CategoryMapper;
import com.example.ecommerge.models.CategoryDTO;
import com.example.ecommerge.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryDTO saveNewCategory(CategoryDTO categoryDTO) {
        return categoryMapper.categoryToCategoryDTO(
                categoryRepository.save(
                        categoryMapper.categoryDTOToCategory(categoryDTO)));
    }

    @Override
    public Page<CategoryDTO> pageCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::categoryToCategoryDTO);
    }

    @Transactional
    @Override
    public void deleteCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException(); // ✅ throw ở Service
        }
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    @Override
    public void updateCategory(UUID categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NotFoundException::new); // ✅ throw ở Service
        category.setCategoryName(categoryDTO.getCategoryName());
        categoryRepository.save(category);
    }

    @Override
    public Optional<CategoryDTO> findById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .map(categoryMapper::categoryToCategoryDTO);
    }
}