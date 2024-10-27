package com.example.cart_service.DTO.Response;

import com.example.cart_service.DTO.Request.ProductRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private String cartID;
    private String username;
    private List<ProductRequest> productList;
}
