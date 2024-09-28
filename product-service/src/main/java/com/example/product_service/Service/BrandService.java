package com.example.product_service.Service;

import com.example.product_service.DTO.Request.BrandRequest;
import com.example.product_service.DTO.Response.BrandResponse;

import java.util.List;

public interface BrandService {
    BrandResponse addBrand(BrandRequest request);

    BrandResponse getBrand(String value);

    List<BrandResponse> getAllBrands();

    BrandResponse updateBrand(String brandName, BrandRequest request);

    String deleteBrand(String value);

    String deleteBrandByName(String brandName);

}
