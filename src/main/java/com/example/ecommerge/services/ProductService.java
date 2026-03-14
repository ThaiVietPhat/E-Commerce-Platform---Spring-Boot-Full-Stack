package com.example.ecommerge.services;

import com.example.ecommerge.models.ProductDTO;

import java.util.UUID;

public interface ProductService {
    ProductDTO addProduct(UUID categoryId, ProductDTO productDTO);
}
