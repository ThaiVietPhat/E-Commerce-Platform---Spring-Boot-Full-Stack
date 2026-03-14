package com.example.ecommerge.services;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.entities.Product;
import com.example.ecommerge.exceptions.NotFoundException;
import com.example.ecommerge.mappers.CategoryMapper;
import com.example.ecommerge.mappers.ProductMapper;
import com.example.ecommerge.models.CategoryDTO;
import com.example.ecommerge.models.ProductDTO;
import com.example.ecommerge.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    @Override
    public ProductDTO addProduct(UUID categoryId, ProductDTO productDTO) {
        CategoryDTO categoryDTO = categoryService.findById(categoryId)
                .orElseThrow(NotFoundException::new);
        Product product = productMapper.productDTOToProduct(productDTO);
        product.setCategory(categoryMapper.categoryDTOToCategory(categoryDTO));
        return productMapper.productToProductDTO(productRepository.save(product));
    }
}
