package com.example.product_service.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String categoryID;
    private String categoryName;
    private String categoryDescription;
    private Timestamp created_at;
    private Timestamp updated_at;
    private boolean isActive;
    private String meta_title;
    private String meta_description;
    private String meta_keywords;
}
