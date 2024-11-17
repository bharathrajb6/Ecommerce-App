package com.example.product_service.Controller;

import com.example.product_service.DTO.Request.ProductRequest;
import com.example.product_service.DTO.Response.ApiResponse;
import com.example.product_service.DTO.Response.ProductResponse;
import com.example.product_service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    ProductService productService;

    /***
     * This method is responsible for handling POST requests to add a new product.
     * @param request
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    /***
     * This method is responsible for handling GET requests to get a product by its ID.
     * @param value
     * @return
     */
    @RequestMapping(value = "/product/{value}", method = RequestMethod.GET)
    public ProductResponse getProducts(@PathVariable String value) {
        return productService.getProduct(value);
    }

    /***
     * This method is responsible for handling GET requests to get all products.
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    /***
     * This method is responsible for handling PUT requests to update a product.
     * @param prodID
     * @param request
     * @return
     */
    @RequestMapping(value = "/product/{productID}", method = RequestMethod.PUT)
    public ProductResponse updateProduct(@PathVariable String prodID, @RequestBody ProductRequest request) {
        return productService.updateProduct(prodID, request);
    }

    /***
     * This method is responsible for handling DELETE requests to delete a product.
     * @param value
     * @return
     */
    @RequestMapping(value = "/product/{value}", method = RequestMethod.DELETE)
    public String deleteProduct(@PathVariable String value) {
        return productService.deleteProduct(value);
    }

    /***
     * This method is responsible for handling GET requests to get all products by category.
     * @param criteria
     * @return
     */
    @RequestMapping(value = "/product/search/{criteria}", method = RequestMethod.GET)
    public List<ProductResponse> searchProduct(@PathVariable String criteria) {
        return productService.searchProduct(criteria);
    }

    @RequestMapping(value = "/product/{prodID}/stock", method = RequestMethod.GET)
    public int getStock(@PathVariable String prodID) {
        return productService.getProductStock(prodID);
    }


    @RequestMapping(value = "/product/{prodID}/stock",method = RequestMethod.PUT)
    public ProductResponse updateProductStock(@PathVariable String prodID, @RequestBody int newStock){
        return productService.updateProductStock(prodID,newStock);
    }
}
