package com.example.ecommerge.models.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCSVRecord {
    @CsvBindByName(column = "category_id") // Khớp với header CSV
    private UUID id;

    @CsvBindByName(column = "category_name") // Khớp với header CSV
    private String categoryName;
}
