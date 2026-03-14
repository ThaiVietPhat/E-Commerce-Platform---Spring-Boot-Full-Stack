package com.example.ecommerge.mappers;

import com.example.ecommerge.entities.Product;
import com.example.ecommerge.models.ProductDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    Product productDTOToProduct(ProductDTO productDTO);      // DTO → Entity
    ProductDTO productToProductDTO(Product product);
}
