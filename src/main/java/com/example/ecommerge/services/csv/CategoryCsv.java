package com.example.ecommerge.services.csv;

import com.example.ecommerge.models.csv.CategoryCSVRecord;

import java.io.File;
import java.util.List;

public interface CategoryCsv {
    List<CategoryCSVRecord> convertCSV(File file) ;
}
