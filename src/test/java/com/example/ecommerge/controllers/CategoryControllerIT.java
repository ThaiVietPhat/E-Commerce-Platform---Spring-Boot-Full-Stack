package com.example.ecommerge.controllers;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.models.CategoryDTO;
import com.example.ecommerge.repositories.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest          // load full Spring context
@AutoConfigureMockMvc    // tạo MockMvc thật
@Testcontainers          // bật TestContainers
class CategoryControllerIT {

    // Khởi động MySQL container 1 lần cho toàn bộ test class
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // Override config DB bằng TestContainers
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CategoryRepository categoryRepository;

    CategoryDTO testCategoryDTO;

    @BeforeEach
    void setUp() {
        // xóa data trước mỗi test để tránh ảnh hưởng nhau
        categoryRepository.deleteAll();

        testCategoryDTO = CategoryDTO.builder()
                .categoryName("Beer Category")
                .build();
    }

    @Test
    void testCreateCategory() throws Exception {
        // POST tạo category
        MvcResult result = mockMvc.perform(post(CategoryController.CATEGORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        // verify thẳng trong DB
        assertThat(categoryRepository.count()).isEqualTo(1);

        // lấy ID từ Location header và verify
        String location = result.getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf("/") + 1);
        assertThat(categoryRepository.findById(UUID.fromString(id))).isPresent();
    }

    @Test
    void testCreateCategory_InvalidName_ReturnsBadRequest() throws Exception {
        CategoryDTO invalid = CategoryDTO.builder()
                .categoryName("")   // blank → validation fail
                .build();

        mockMvc.perform(post(CategoryController.CATEGORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        // verify DB không có gì
        assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @Test
    void testGetAllCategories() throws Exception {
        // tạo sẵn data trong DB
        categoryRepository.save(Category.builder()
                .categoryName("Beer Category")
                .build());
        categoryRepository.save(Category.builder()
                .categoryName("Wine Category")
                .build());

        mockMvc.perform(get(CategoryController.CATEGORY_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", greaterThan(0)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void testUpdateCategory() throws Exception {
        // tạo sẵn data trong DB
        Category saved = categoryRepository.save(Category.builder()
                .categoryName("Old Name")
                .build());

        CategoryDTO updateDTO = CategoryDTO.builder()
                .categoryName("New Name")
                .build();

        mockMvc.perform(put(CategoryController.CATEGORY_PATH_ID, saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNoContent());

        // verify DB đã update
        Category updated = categoryRepository.findById(saved.getId()).get();
        assertThat(updated.getCategoryName()).isEqualTo("New Name");
    }

    @Test
    void testUpdateCategory_NotFound() throws Exception {
        mockMvc.perform(put(CategoryController.CATEGORY_PATH_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategory() throws Exception {
        // tạo sẵn data trong DB
        Category saved = categoryRepository.save(Category.builder()
                .categoryName("To Delete")
                .build());

        mockMvc.perform(delete(CategoryController.CATEGORY_PATH_ID, saved.getId()))
                .andExpect(status().isNoContent());

        // verify đã xóa khỏi DB
        assertThat(categoryRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        mockMvc.perform(delete(CategoryController.CATEGORY_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}