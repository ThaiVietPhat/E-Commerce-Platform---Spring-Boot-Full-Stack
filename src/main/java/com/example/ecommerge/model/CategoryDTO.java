package com.example.ecommerge.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Builder
@Data
public class CategoryDTO {
    private UUID id;
    @NotBlank
    @Size(min=5, max = 50)
    private String categoryName;
}
