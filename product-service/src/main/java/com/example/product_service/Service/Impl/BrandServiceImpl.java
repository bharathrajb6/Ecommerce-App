package com.example.product_service.Service.Impl;

import com.example.product_service.DTO.Request.BrandRequest;
import com.example.product_service.DTO.Response.BrandResponse;
import com.example.product_service.Exceptions.BrandExceptions;
import com.example.product_service.Mapper.BrandMapper;
import com.example.product_service.Model.Brand;
import com.example.product_service.Repository.BrandRepository;
import com.example.product_service.Service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.product_service.Messages.Brand.BrandExceptionMessages.*;
import static com.example.product_service.Messages.Brand.BrandLogMessages.*;
import static com.example.product_service.Utils.CommonUtils.UUIDChecker;
import static com.example.product_service.validations.BrandValidationHandler.validateBrandDetails;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private RedisServiceImpl redisService;

    /***
     * Add a new brand to the database.
     * @param request
     * @return
     */
    @Override
    public BrandResponse addBrand(BrandRequest request) {

        // Check if brand is already present in database
        if (brandRepository.existsByBrandName(request.getBrandName())) {
            log.error(LOG_BRAND_WITH_SAME_NAME);
            throw new BrandExceptions(EXCEPTION_BRAND_ALREADY_EXISTS);
        }

        Brand brand = brandMapper.toBrand(request);
        brand.setBrandID(UUID.randomUUID().toString());
        brand.setBrandName(brand.getBrandName().toLowerCase());
        brand.setCreatedDate(Timestamp.from(Instant.now()));
        brand.setUpdatedDate(Timestamp.from(Instant.now()));

        // Validate the brand information
        validateBrandDetails(brand);

        try {
            // Save the brand information to database
            brandRepository.save(brand);
            log.info(LOG_BRAND_SAVED_SUCCESSFULLY, brand.getBrandName());
            return brandMapper.toBrandResponse(brand);
        } catch (Exception e) {
            log.info(LOG_UNABLE_TO_SAVE_BRAND, brand.getBrandName(), e.getMessage());

            // Throw the error if issues occured while saving the information
            throw new BrandExceptions(String.format(EXCEPTION_UNABLE_TO_SAVE_BRAND, brand.getBrandName()));
        }
    }

    /***
     * Get the brand details based on the brand name or brand ID.
     * @param value
     * @return
     */
    @Override
    public BrandResponse getBrand(String value) {
        // Check if the value is UUID
        if (UUIDChecker(value)) {
            // Check if brand information is present in cache
            BrandResponse brandResponse = redisService.getData(value, BrandResponse.class);
            if (brandResponse != null) {
                return brandResponse;
            } else {
                // If it is not present in cache, then load the brand information from database
                Brand brand = brandRepository.findByBrandID(value).orElseThrow(() -> {
                    return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_ID);
                });

                // Save the brand information to database
                redisService.setData(brand.getBrandID(), brand, 300L);
                return brandMapper.toBrandResponse(brand);
            }
        } else {
            return getBrandByName(value);
        }
    }

    /**
     * This method will return brand information based on brand name
     *
     * @param value
     * @return
     */
    public BrandResponse getBrandByName(String value) {
        // Check if brand information is present in cache
        BrandResponse brandResponse = redisService.getData(value, BrandResponse.class);
        if (brandResponse != null) {
            return brandResponse;
        } else {
            // If it is not present in cache, the load it from database
            Brand brand = brandRepository.findByBrandName(value).orElseThrow(() -> {
                return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_NAME);
            });

            // Save the brand information to cache
            redisService.setData(brand.getBrandName(), brand, 300L);
            return brandMapper.toBrandResponse(brand);
        }
    }

    /***
     * Get all the brands from the database.
     * @return
     */
    @Override
    public List<BrandResponse> getAllBrands() {
        // Check if brandList is available in cache
        List<BrandResponse> brandResponses = redisService.getData("brandList", List.class);
        if (brandResponses != null) {
            return brandResponses;
        } else {
            // If it's not there, then load it from database
            List<Brand> brandList = brandRepository.findAll();

            // Add the info to cache
            redisService.setData("brandList", brandList, 300L);
            return brandMapper.toBrandResponseList(brandList);
        }
    }

    /***
     * Update the brand details based on the brand name.
     * @param brandName
     * @param request
     * @return
     */
    @Override
    public BrandResponse updateBrand(String brandName, BrandRequest request) {
        // Check if brand is exist with this name
        Brand brand = brandRepository.findByBrandName(brandName).orElseThrow(() -> {
            return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_NAME);
        });
        brandMapper.updateBrandDetails(brand, request);

        // Validate the brand
        validateBrandDetails(brand);

        try {
            // Update the brand with latest info
            brandRepository.updateBrandByID(brand.getBrandName(), brand.getBrandDescription(), Timestamp.from(Instant.now()), brand.getBrandID());
            log.info(LOG_BRAND_UPDATED_SUCCESSFULLY, brand.getBrandName());

            // Delete the data from cache to avoid mismatch data
            redisService.deleteData(brandName);
            redisService.deleteData(brand.getBrandID());
            redisService.deleteData("brandList");

            return getBrand(brandName);
        } catch (org.springframework.dao.DataIntegrityViolationException integrityViolationException) {
            log.error(LOG_UNABLE_TO_UPDATE_BRAND, brand.getBrandName(), integrityViolationException.getMessage());
            throw new BrandExceptions("Data integrity violation while updating brand.", integrityViolationException.getCause());
        }
    }


    /***
     * Delete the brand based on the brand name or brand ID.
     * @param value
     * @return
     */
    @Override
    public String deleteBrand(String value) {
        // If the value is UUID, then delete the brand based on UUID.
        if (UUIDChecker(value)) {
            return brandRepository.findByBrandID(value).map(brand -> {
                try {
                    brandRepository.deleteByBrandID(value);
                    redisService.deleteData(value);
                    redisService.deleteData("brandList");
                    redisService.deleteData(brand.getBrandName());
                    log.info(LOG_BRAND_DELETED_SUCCESSFULLY, brand.getBrandName());
                    return String.format(EXCEPTION_BRAND_DELETED_SUCCESSFULLY, brand.getBrandName());
                } catch (Exception e) {
                    log.error(LOG_UNABLE_TO_DELETE_BRAND, brand.getBrandName(), e.getMessage());
                    throw new BrandExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_BRAND, brand.getBrandName()));
                }
            }).orElseThrow(() -> {
                log.error(LOG_BRAND_NOT_FOUND_WITH_ID, value);
                return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_ID);
            });
        } else {
            // Otherwise, delete the brand based on brand name.
            return deleteBrandByName(value);
        }
    }

    /***
     * Delete the brand based on the brand name.
     * @param brandName
     * @return
     */
    @Override
    public String deleteBrandByName(String brandName) {
        return brandRepository.findByBrandName(brandName).map(brand -> {
            try {
                brandRepository.deleteByBrandName(brandName);
                redisService.deleteData(brandName);
                redisService.deleteData("brandList");
                redisService.deleteData(brand.getBrandID());
                log.info(LOG_BRAND_DELETED_SUCCESSFULLY, brandName);
                return String.format(EXCEPTION_BRAND_DELETED_SUCCESSFULLY, brandName);
            } catch (Exception e) {
                log.error(LOG_UNABLE_TO_DELETE_BRAND, brandName, e.getMessage());
                throw new BrandExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_BRAND, brandName));
            }
        }).orElseThrow(() -> {
            log.error(LOG_BRAND_NOT_FOUND_WITH_NAME, brandName);
            return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_NAME);
        });
    }
}
