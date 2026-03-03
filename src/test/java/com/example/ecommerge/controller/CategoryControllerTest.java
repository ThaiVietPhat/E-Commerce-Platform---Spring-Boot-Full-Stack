package com.example.ecommerge.controller;

import com.example.ecommerge.model.CategoryDTO;
import com.example.ecommerge.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(CategoryController.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    CategoryService categoryService;
    CategoryDTO test1CategoryDTO;
    CategoryDTO test2CategoryDTO;
    CategoryDTO test3CategoryDTO;
    List<CategoryDTO> categoriesListTest;
    @Autowired
    ObjectMapper objectMapper;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<CategoryDTO> categoryArgumentCaptor;
    @BeforeEach
    void setUp() {
        categoriesListTest=new ArrayList<>();
        test1CategoryDTO = CategoryDTO.builder()
                .id(UUID.randomUUID())
                .categoryName("Phat")
                .build();
        test2CategoryDTO = CategoryDTO.builder()
                .id(UUID.randomUUID())
                .categoryName("Huyen")
                .build();
        categoriesListTest.add(test1CategoryDTO);
        categoriesListTest.add(test2CategoryDTO);
    }
    @Test
    void getAllCategories() throws Exception {
        //given
        given(categoryService.listCategories()).willReturn(categoriesListTest);
        //when
        mockMvc.perform(get(CategoryController.CATEGORY_PATH)
                .accept(MediaType.APPLICATION_JSON)
        )
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));

    }

    @Test
    void testCreateNewCategory() throws Exception {
        CategoryDTO newCategoryDTO = CategoryDTO.builder()
                .id(null)
                .categoryName("test")
                .build();
        given(categoryService.savedNewCategory(any(CategoryDTO.class))).willReturn(test2CategoryDTO);
        mockMvc.perform(post(CategoryController.CATEGORY_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategoryDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateTestCategory() throws Exception {
        CategoryDTO newCategoryDTO = CategoryDTO.builder()
                .id(UUID.randomUUID())
                .categoryName("test")
                .build();
        given(categoryService.updateCategory(any(UUID.class), any(CategoryDTO.class))).willReturn(Optional.of(newCategoryDTO));
        mockMvc.perform(put(CategoryController.CATEGORY_PATH_ID, newCategoryDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCategoryDTO))
        ).andExpect(status().isNoContent());
        verify(categoryService).updateCategory(any(UUID.class), any(CategoryDTO.class));
    }

    @Test
    void testDeleteCategory() throws Exception {
        given(categoryService.deleteCategory(any())).willReturn(true);
        mockMvc.perform(delete(CategoryController.CATEGORY_PATH_ID, test2CategoryDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(uuidArgumentCaptor.capture());
        assertThat(test2CategoryDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
    @Test
    void testCreateCategoryNullCategoryName() throws Exception {
        CategoryDTO categoryDTO = CategoryDTO.builder().build();
        given(categoryService.savedNewCategory(any(CategoryDTO.class))).willReturn(test2CategoryDTO);
        MvcResult mvcResult =mockMvc.perform(post(CategoryController.CATEGORY_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}