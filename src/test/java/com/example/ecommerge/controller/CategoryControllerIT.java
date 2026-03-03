package com.example.ecommerge.controller;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.exceptions.NotFoundException;
import com.example.ecommerge.mappers.CategoryMapper;
import com.example.ecommerge.model.CategoryDTO;
import com.example.ecommerge.repositories.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CategoryControllerIT {
    @Autowired
    CategoryController categoryController;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void testUpdateCategoryNotFound(){
        assertThrows(NotFoundException.class, () -> categoryController.updateCategory(UUID.randomUUID(), CategoryDTO.builder().build()));

    }
    @Test
    void testListCategory(){
        List<CategoryDTO> categories = categoryController.getAllCategories();
        assertThat(categories.size()).isEqualTo(2);
    }
    @Test
    @Rollback
    @Transactional
    void testEmptyList(){
        categoryRepository.deleteAll();
        List<CategoryDTO> categories = categoryController.getAllCategories();
        assertThat(categories.size()).isEqualTo(0);
    }
    @Test
    @Rollback
    @Transactional
    void testNewCategory(){
        CategoryDTO categoryDTO = CategoryDTO.builder().categoryName("New Category").build();
        ResponseEntity responseEntity = categoryController.handlePost(categoryDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationUUID = responseEntity.getHeaders().getLocation().toString().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Category category = categoryRepository.findById(savedUUID).get();
        assertThat(category).isNotNull();
    }
    @Rollback
    @Transactional
    @Test
    void testUpdateCategory(){
        Category category = categoryRepository.findAll().get(0);
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
        categoryDTO.setId(null);
        categoryDTO.setCategoryName("Updated Category");
        ResponseEntity responseEntity = categoryController.updateCategory(category.getId(), categoryDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        assertThat(updatedCategory.getCategoryName()).isEqualTo("Updated Category");
    }
    @Rollback
    @Transactional
    @Test
    void testDeleteCategory(){
        Category category = categoryRepository.findAll().get(0);
        ResponseEntity responseEntity = categoryController.deleteCategoryById(category.getId());
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);
        assertThat(categoryRepository.findById(category.getId())).isEmpty();
    }
    @Test
    void testDeleteCategoryNotFound(){
        assertThrows(NotFoundException.class, () -> categoryController.deleteCategoryById(UUID.randomUUID()));
    }

}
