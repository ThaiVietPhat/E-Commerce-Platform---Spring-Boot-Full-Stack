package com.example.ecommerge.controllers;

import com.example.ecommerge.exceptions.NotFoundException;
import com.example.ecommerge.models.CategoryDTO;
import com.example.ecommerge.models.PageResponse;
import com.example.ecommerge.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;
@RequiredArgsConstructor
@RestController
public class CategoryController {
    public static final String CATEGORY_PATH    = "/api/v1/categories";
    public static final String CATEGORY_PATH_ID = CATEGORY_PATH + "/{id}";
    private final CategoryService categoryService;

    @GetMapping(CATEGORY_PATH)
    public ResponseEntity<PageResponse<CategoryDTO>> getAllCategories(
            @PageableDefault(page = 0, size = 25, sort = "categoryName") Pageable pageable) {
        return ResponseEntity.ok(
                PageResponse.of(categoryService.pageCategories(pageable)));
    }

    @PostMapping(CATEGORY_PATH)
    public ResponseEntity<Void> handlePost(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO saved = categoryService.saveNewCategory(categoryDTO);
        return ResponseEntity.created(
                        URI.create(CATEGORY_PATH + "/" + saved.getId()))
                .build();
    }

    @PutMapping(CATEGORY_PATH_ID)
    public ResponseEntity<Void> updateCategory(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(CATEGORY_PATH_ID)
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id") UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}