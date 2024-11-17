package com.example.user_service.config;

import com.example.user_service.DTO.Response.ErrorResponse;
import com.example.user_service.Exceptions.OrderException;
import com.example.user_service.Exceptions.ProductException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.example.user_service.messages.OrderMessages.ORDER_ERROR;
import static com.example.user_service.messages.ProductMessages.PRODUCT_ERROR;

@Component
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String responseBody = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

            if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                if (Objects.equals(errorResponse.getError(), PRODUCT_ERROR)) {
                    return new ProductException(errorResponse.getMessage());
                }
                if (Objects.equals(errorResponse.getError(), ORDER_ERROR)) {
                    return new OrderException(errorResponse.getMessage());
                }
            }
        } catch (IOException e) {
            return new Exception("Error decoding Feign response", e);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}

