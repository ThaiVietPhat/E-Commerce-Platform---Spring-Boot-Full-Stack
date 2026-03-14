package com.example.ecommerge.controllers;

import com.example.ecommerge.entities.Product;
import com.example.ecommerge.models.ProductDTO;
import com.example.ecommerge.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;
@RequiredArgsConstructor
@RestController
public class CategoryProductController {
    private final ProductService productService;
    private static final String CATEGORY_PRODUCT_PATH = "/api/v1/admin/categories/{categoryId}/products";
    @PostMapping(CATEGORY_PRODUCT_PATH)
    public ResponseEntity<ProductDTO> addProductToCategory(@Valid @RequestBody ProductDTO productDTO,
                                                           @PathVariable UUID categoryId){
        ProductDTO save = productService.addProduct(categoryId, productDTO);
        return ResponseEntity.created(
                        URI.create("/api/v1/public/products/" + save.getId()))
                .build();
    }

}
