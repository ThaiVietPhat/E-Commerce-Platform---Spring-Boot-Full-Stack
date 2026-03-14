package com.example.ecommerge.bootstraps;

import com.example.ecommerge.entities.Category;
import com.example.ecommerge.models.csv.CategoryCSVRecord;
import com.example.ecommerge.repositories.CategoryRepository;
import com.example.ecommerge.services.csv.CategoryCsv;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapDataCategory implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final CategoryCsv categoryCsv;
    @Override
    public void run(String... args) throws Exception {
        loadCsvData();
    }
    private void loadCsvData() throws FileNotFoundException {
        if (categoryRepository.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/Categories.csv");

            // Dùng service để đọc list record
            List<CategoryCSVRecord> csvRecords = categoryCsv.convertCSV(file);

            // Lặp qua list để chuyển từ Record sang Entity và lưu vào DB
            csvRecords.forEach(record -> {
                categoryRepository.save(Category.builder()
                        .categoryName(record.getCategoryName())
                        .build());
            });
        }
    }

}
