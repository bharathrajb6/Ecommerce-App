package com.example.product_service.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {
    private String brandID;
    private String brandName;
    private String brandDescription;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
