package com.example.ecommerge.repositories;
import com.example.ecommerge.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class CategoryRepositoryIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void testSaveCategory() {
        Category saved = categoryRepository.save(Category.builder()
                .categoryName("Beer Category")
                .build());

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCategoryName()).isEqualTo("Beer Category");
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getLastModifiedDate()).isNotNull();
    }

    @Test
    void testFindAll_Pageable() {
        categoryRepository.save(Category.builder().categoryName("Beer").build());
        categoryRepository.save(Category.builder().categoryName("Wine").build());
        categoryRepository.save(Category.builder().categoryName("Juice").build());

        Page<Category> page = categoryRepository.findAll(PageRequest.of(0, 2));

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent()).hasSize(2); // page size = 2
    }

    @Test
    void testDeleteCategory() {
        Category saved = categoryRepository.save(Category.builder()
                .categoryName("To Delete")
                .build());

        categoryRepository.deleteById(saved.getId());

        assertThat(categoryRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void testExistsById() {
        Category saved = categoryRepository.save(Category.builder()
                .categoryName("Beer")
                .build());

        assertThat(categoryRepository.existsById(saved.getId())).isTrue();
    }
}