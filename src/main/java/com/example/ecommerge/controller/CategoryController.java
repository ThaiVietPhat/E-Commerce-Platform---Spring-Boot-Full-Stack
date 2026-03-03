package com.example.ecommerge.controller;

import com.example.ecommerge.exceptions.NotFoundException;
import com.example.ecommerge.model.CategoryDTO;
import com.example.ecommerge.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    public static final String CATEGORY_PATH = "/api/public/category";
    public static final String CATEGORY_PATH_ID =  CATEGORY_PATH+"/{categoryId}";
    private final CategoryService categoryService;
    @PutMapping(CATEGORY_PATH_ID)
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable UUID categoryId, @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryId, categoryDTO).orElseThrow(NotFoundException::new);
        return ResponseEntity.noContent().build();
    }
    @GetMapping(CATEGORY_PATH)
    public List<CategoryDTO> getAllCategories()
    {
        return categoryService.listCategories();
    }
    @PostMapping(CATEGORY_PATH)
    public ResponseEntity<CategoryDTO> handlePost(@Valid @RequestBody CategoryDTO categoryDTO)
    {
        CategoryDTO savedCategoryDTO = categoryService.savedNewCategory(categoryDTO);
        return ResponseEntity.created(URI.create(CATEGORY_PATH + "/" + savedCategoryDTO.getId().toString()))
                .build();
    }
    @DeleteMapping(CATEGORY_PATH_ID)
    public ResponseEntity<CategoryDTO> deleteCategoryById(@Valid @PathVariable UUID categoryId){
        if(!categoryService.deleteCategory(categoryId)){
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }
}
