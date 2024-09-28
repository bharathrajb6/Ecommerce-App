package com.example.product_service.Mapper;

import com.example.product_service.DTO.Request.BrandRequest;
import com.example.product_service.DTO.Response.BrandResponse;
import com.example.product_service.Model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/***
 * This is a Mapper interface for Brand entity
 */
@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandRequest brandRequest);

    BrandResponse toBrandResponse(Brand brand);

    List<BrandResponse> toBrandResponseList(List<Brand> brands);

    void updateBrandDetails(@MappingTarget Brand brand, BrandRequest brandRequest);
}
