package com.example.product_service.Mapper;

import com.example.product_service.DTO.Request.BrandRequest;
import com.example.product_service.DTO.Response.BrandResponse;
import com.example.product_service.Model.Brand;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-28T22:11:36+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Azul Systems, Inc.)"
)
@Component
public class BrandMapperImpl implements BrandMapper {

    @Override
    public Brand toBrand(BrandRequest brandRequest) {
        if ( brandRequest == null ) {
            return null;
        }

        Brand brand = new Brand();

        brand.setBrandName( brandRequest.getBrandName() );
        brand.setBrandDescription( brandRequest.getBrandDescription() );

        return brand;
    }

    @Override
    public BrandResponse toBrandResponse(Brand brand) {
        if ( brand == null ) {
            return null;
        }

        BrandResponse.BrandResponseBuilder brandResponse = BrandResponse.builder();

        brandResponse.brandID( brand.getBrandID() );
        brandResponse.brandName( brand.getBrandName() );
        brandResponse.brandDescription( brand.getBrandDescription() );
        brandResponse.createdDate( brand.getCreatedDate() );
        brandResponse.updatedDate( brand.getUpdatedDate() );

        return brandResponse.build();
    }

    @Override
    public List<BrandResponse> toBrandResponseList(List<Brand> brands) {
        if ( brands == null ) {
            return null;
        }

        List<BrandResponse> list = new ArrayList<BrandResponse>( brands.size() );
        for ( Brand brand : brands ) {
            list.add( toBrandResponse( brand ) );
        }

        return list;
    }

    @Override
    public void updateBrandDetails(Brand brand, BrandRequest brandRequest) {
        if ( brandRequest == null ) {
            return;
        }

        brand.setBrandName( brandRequest.getBrandName() );
        brand.setBrandDescription( brandRequest.getBrandDescription() );
    }
}
