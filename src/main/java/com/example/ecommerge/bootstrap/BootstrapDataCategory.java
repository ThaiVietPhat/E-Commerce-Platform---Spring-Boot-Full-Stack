package com.example.ecommerge.bootstrap;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapDataCategory implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    @Override
    public void run(String... args) throws Exception {
        loadCategoryData();
    }
    private void loadCategoryData() {
        if(categoryRepository.count() == 0) {
            Category category1 = Category.builder().categoryName("Phat").build();
            Category category2 = Category.builder().categoryName("Huyen").build();
            categoryRepository.save(category1);
            categoryRepository.save(category2);
        }
    }
}
