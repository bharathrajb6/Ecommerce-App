package com.example.product_service.Service.Impl;

import com.example.product_service.DTO.Request.BrandRequest;
import com.example.product_service.DTO.Response.BrandResponse;
import com.example.product_service.Exceptions.BrandExceptions;
import com.example.product_service.Mapper.BrandMapper;
import com.example.product_service.Model.Brand;
import com.example.product_service.Repository.BrandRepository;
import com.example.product_service.Service.BrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    BrandMapper brandMapper;

    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    /***
     * Add a new brand to the database.
     * @param request
     * @return
     */
    @Override
    public BrandResponse addBrand(BrandRequest request) {
        if (brandRepository.existsByBrandName(request.getBrandName())) {
            logger.error(LOG_BRAND_WITH_SAME_NAME);
            throw new BrandExceptions(EXCEPTION_BRAND_ALREADY_EXISTS);
        }
        Brand brand = brandMapper.toBrand(request);
        brand.setBrandID(UUID.randomUUID().toString());
        brand.setBrandName(brand.getBrandName().toLowerCase());
        brand.setCreatedDate(Timestamp.from(Instant.now()));
        brand.setUpdatedDate(Timestamp.from(Instant.now()));
        validateBrandDetails(brand);
        try {
            brandRepository.save(brand);
            logger.info(LOG_BRAND_SAVED_SUCCESSFULLY, brand.getBrandName());
            return brandMapper.toBrandResponse(brand);
        } catch (Exception e) {
            logger.info(LOG_UNABLE_TO_SAVE_BRAND, brand.getBrandName(), e.getMessage());
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
        if (UUIDChecker(value)) {
            Brand brand = brandRepository.findByBrandID(value).orElseThrow(() -> {
                return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_ID);
            });
            return brandMapper.toBrandResponse(brand);
        } else {
            Brand brand = brandRepository.findByBrandName(value).orElseThrow(() -> {
                return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_NAME);
            });
            return brandMapper.toBrandResponse(brand);
        }
    }

    /***
     * Get all the brands from the database.
     * @return
     */
    @Override
    public List<BrandResponse> getAllBrands() {
        return brandMapper.toBrandResponseList(brandRepository.findAll());
    }

    /***
     * Update the brand details based on the brand name.
     * @param brandName
     * @param request
     * @return
     */
    @Override
    public BrandResponse updateBrand(String brandName, BrandRequest request) {
        Brand brand = brandRepository.findByBrandName(brandName).orElseThrow(() -> {
            return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_NAME);
        });
        brandMapper.updateBrandDetails(brand, request);
        validateBrandDetails(brand);
        try {
            int result = brandRepository.updateBrandByID(brand.getBrandName(), brand.getBrandDescription(), Timestamp.from(Instant.now()), brand.getBrandID());
            logger.info(LOG_BRAND_UPDATED_SUCCESSFULLY, brand.getBrandName());
            return getBrand(brandName);
        } catch (org.springframework.dao.DataIntegrityViolationException integrityViolationException) {
            logger.error(LOG_UNABLE_TO_UPDATE_BRAND, brand.getBrandName(), integrityViolationException.getMessage());
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
                    logger.info(LOG_BRAND_DELETED_SUCCESSFULLY, brand.getBrandName());
                    return String.format(EXCEPTION_BRAND_DELETED_SUCCESSFULLY, brand.getBrandName());
                } catch (Exception e) {
                    logger.error(LOG_UNABLE_TO_DELETE_BRAND, brand.getBrandName(), e.getMessage());
                    throw new BrandExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_BRAND, brand.getBrandName()));
                }
            }).orElseThrow(() -> {
                logger.error(LOG_BRAND_NOT_FOUND_WITH_ID, value);
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
                logger.info(LOG_BRAND_DELETED_SUCCESSFULLY, brandName);
                return String.format(EXCEPTION_BRAND_DELETED_SUCCESSFULLY, brandName);
            } catch (Exception e) {
                logger.error(LOG_UNABLE_TO_DELETE_BRAND, brandName, e.getMessage());
                throw new BrandExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_BRAND, brandName));
            }
        }).orElseThrow(() -> {
            logger.error(LOG_BRAND_NOT_FOUND_WITH_NAME, brandName);
            return new BrandExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_NAME);
        });
    }
}
