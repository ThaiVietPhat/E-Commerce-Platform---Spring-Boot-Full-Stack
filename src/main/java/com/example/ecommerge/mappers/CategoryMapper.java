package com.example.ecommerge.mappers;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.model.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    Category categoryDTOToCategory(CategoryDTO categoryDTO);
    CategoryDTO categoryToCategoryDTO(Category category);
}
