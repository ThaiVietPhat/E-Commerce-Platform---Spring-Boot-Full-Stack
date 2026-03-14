package com.example.ecommerge.services;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.mappers.CategoryMapper;
import com.example.ecommerge.models.CategoryDTO;
import com.example.ecommerge.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryMapper categoryMapper;

    @InjectMocks
    CategoryServiceImpl categoryService;

    Category testCategory;
    CategoryDTO testCategoryDTO;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(UUID.randomUUID())
                .categoryName("Beer Category")
                .version(1)
                .build();

        testCategoryDTO = CategoryDTO.builder()
                .id(testCategory.getId())
                .categoryName("Beer Category")
                .version(1)
                .build();
    }

    @Test
    void testSaveNewCategory() {
        // Mock mapper và repository
        given(categoryMapper.categoryDTOToCategory(any())).willReturn(testCategory);
        given(categoryRepository.save(any())).willReturn(testCategory);
        given(categoryMapper.categoryToCategoryDTO(any())).willReturn(testCategoryDTO);

        CategoryDTO result = categoryService.savedNewCategory(testCategoryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getCategoryName()).isEqualTo("Beer Category");
        verify(categoryRepository).save(any());
    }

    @Test
    void testPageCategories() {
        given(categoryRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(testCategory)));
        given(categoryMapper.categoryToCategoryDTO(any())).willReturn(testCategoryDTO);

        var result = categoryService.pageCategories(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategoryName()).isEqualTo("Beer Category");
    }

    @Test
    void testDeleteCategory_Exists() {
        given(categoryRepository.existsById(any())).willReturn(true);

        boolean result = categoryService.deleteCategory(testCategory.getId());

        assertThat(result).isTrue();
        verify(categoryRepository).deleteById(any());
    }

    @Test
    void testDeleteCategory_NotFound() {
        given(categoryRepository.existsById(any())).willReturn(false);

        boolean result = categoryService.deleteCategory(UUID.randomUUID());

        assertThat(result).isFalse();
    }

    @Test
    void testUpdateCategory() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(testCategory));
        given(categoryRepository.save(any())).willReturn(testCategory);
        given(categoryMapper.categoryToCategoryDTO(any())).willReturn(testCategoryDTO);

        Optional<CategoryDTO> result = categoryService.updateCategory(
                testCategory.getId(), testCategoryDTO);

        assertThat(result).isPresent();
        verify(categoryRepository).save(any());
    }

    @Test
    void testUpdateCategory_NotFound() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        Optional<CategoryDTO> result = categoryService.updateCategory(
                UUID.randomUUID(), testCategoryDTO);

        assertThat(result).isEmpty();
    }
}