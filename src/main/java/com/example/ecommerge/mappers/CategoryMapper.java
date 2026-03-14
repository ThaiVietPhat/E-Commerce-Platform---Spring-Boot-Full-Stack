package com.example.ecommerge.mappers;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.models.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    Category categoryDTOToCategory(CategoryDTO categoryDTO);
    CategoryDTO categoryToCategoryDTO(Category category);
}
