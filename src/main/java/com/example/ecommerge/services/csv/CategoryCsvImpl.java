package com.example.ecommerge.services.csv;

import com.example.ecommerge.models.csv.CategoryCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
@Service
public class CategoryCsvImpl implements CategoryCsv {
    @Override
    public List<CategoryCSVRecord> convertCSV(File file) {
        try (FileReader fileReader = new FileReader(file)) {

            List<CategoryCSVRecord> categoryCSVRecords = new CsvToBeanBuilder<CategoryCSVRecord>(fileReader)
                    .withType(CategoryCSVRecord.class)
                    .withIgnoreLeadingWhiteSpace(true) // Nên thêm cái này để tránh lỗi khoảng trắng
                    .build()
                    .parse();

            return categoryCSVRecords;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file CSV: " + e.getMessage(), e);
        }
    }
}