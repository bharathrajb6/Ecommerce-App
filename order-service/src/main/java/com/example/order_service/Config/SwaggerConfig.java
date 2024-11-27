package com.example.order_service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customSwaggerConfig() {
        return new OpenAPI().info(new Info().title("Order-Service").version("1.0").description("API Documentation for Order Service"));
    }

}
