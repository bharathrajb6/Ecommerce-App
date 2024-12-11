package com.example.order_service.Config;

import com.example.order_service.DTO.Response.ErrorResponse;
import com.example.order_service.Exception.OrderException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    private static final String PRODUCT_ERROR = "Product Error";

    private static final String ORDER_ERROR = "Order Error";

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String responseBody = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

            if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                if (Objects.equals(errorResponse.getError(), PRODUCT_ERROR)) {
                    return new OrderException(errorResponse.getMessage());
                }
                if (Objects.equals(errorResponse.getError(), ORDER_ERROR)) {
                    return new OrderException(errorResponse.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
