package com.example.product_service.Controller;

import com.example.product_service.DTO.Request.BrandRequest;
import com.example.product_service.DTO.Response.ApiResponse;
import com.example.product_service.DTO.Response.BrandResponse;
import com.example.product_service.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1")
public class BrandController {
    @Autowired
    BrandService brandService;

    /***
     * This method is responsible for handling POST requests to add a new brand.
     * @param request
     * @return
     */
    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    public ApiResponse<BrandResponse> addBrand(@RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder().result(brandService.addBrand(request)).build();
    }

    /***
     * This method is responsible for handling GET requests to fetch the brand details.
     * @param brandID
     * @return
     */
    @RequestMapping(value = "/brand/{brandID}", method = RequestMethod.GET)
    public ApiResponse<BrandResponse> getBrand(@PathVariable String brandID) {
        return ApiResponse.<BrandResponse>builder().result(brandService.getBrand(brandID)).build();
    }

    /***
     * This method is responsible for handling GET requests to fetch all brand details.
     * @return
     */
    @RequestMapping(value = "/brand", method = RequestMethod.GET)
    public ApiResponse<List<BrandResponse>> getAllBrands() {
        return ApiResponse.<List<BrandResponse>>builder().result(brandService.getAllBrands()).build();
    }

    /***
     * This method is responsible for handling PUT requests to update the brand details.
     * @param brandName
     * @param request
     * @return
     */
    @RequestMapping(value = "/brand/{brandName}", method = RequestMethod.PUT)
    public ApiResponse<BrandResponse> updateBrand(@PathVariable String brandName, @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder().result(brandService.updateBrand(brandName, request)).build();
    }

    /***
     * This method is responsible for handling DELETE requests to delete the brand.
     * @param value
     * @return
     */
    @RequestMapping(value = "/brand/{value}", method = RequestMethod.DELETE)
    public ApiResponse<Void> deleteBrand(@PathVariable String value) {
        return ApiResponse.<Void>builder().message(brandService.deleteBrand(value)).build();
    }
}
