package com.example.product_service.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    private String categoryName;
    private String categoryDescription;
    private boolean isActive;
    private String meta_title;
    private String meta_description;
    private String meta_keywords;
}
