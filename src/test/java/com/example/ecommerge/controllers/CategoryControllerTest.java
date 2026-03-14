package com.example.ecommerge.controllers;

import com.example.ecommerge.exceptions.NotFoundException;
import com.example.ecommerge.models.CategoryDTO;
import com.example.ecommerge.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CategoryService categoryService;

    CategoryDTO testCategoryDTO;

    @BeforeEach
    void setUp() {
        testCategoryDTO = CategoryDTO.builder()
                .id(UUID.randomUUID())
                .categoryName("Beer Category")
                .version(1)
                .build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Mock service trả về Page
        given(categoryService.pageCategories(any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(testCategoryDTO)));

        mockMvc.perform(get(CategoryController.CATEGORY_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryName")
                        .value(testCategoryDTO.getCategoryName()));
    }

    @Test
    void testCreateCategory() throws Exception {
        // Mock service trả về DTO đã save
        given(categoryService.savedNewCategory(any(CategoryDTO.class)))
                .willReturn(testCategoryDTO);

        mockMvc.perform(post(CategoryController.CATEGORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isCreated())
                // kiểm tra Location header có chứa ID
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateCategory_InvalidName_ReturnsBadRequest() throws Exception {
        // categoryName blank → validation fail
        CategoryDTO invalid = CategoryDTO.builder()
                .categoryName("")
                .build();

        mockMvc.perform(post(CategoryController.CATEGORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCategory() throws Exception {
        given(categoryService.updateCategory(any(UUID.class), any(CategoryDTO.class)))
                .willReturn(Optional.of(testCategoryDTO));

        mockMvc.perform(put(CategoryController.CATEGORY_PATH_ID, testCategoryDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isNoContent());

        // verify service được gọi đúng
        verify(categoryService).updateCategory(any(UUID.class), any(CategoryDTO.class));
    }

    @Test
    void testUpdateCategory_NotFound() throws Exception {
        // service trả về empty → controller throw NotFoundException
        given(categoryService.updateCategory(any(UUID.class), any(CategoryDTO.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(put(CategoryController.CATEGORY_PATH_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategory() throws Exception {
        given(categoryService.deleteCategory(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete(CategoryController.CATEGORY_PATH_ID, testCategoryDTO.getId()))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(any(UUID.class));
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        given(categoryService.deleteCategory(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(CategoryController.CATEGORY_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}