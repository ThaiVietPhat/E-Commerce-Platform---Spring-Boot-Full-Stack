package com.example.ecommerge.controllers;

import com.example.ecommerge.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;
    private static final String PRODUCT_PATH = "/api/v1/products";
    private static final String PRODUCT_PATH_ID = PRODUCT_PATH + "/{productId}";
}
