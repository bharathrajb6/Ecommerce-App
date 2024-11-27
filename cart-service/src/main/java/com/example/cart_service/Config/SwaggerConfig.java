package com.example.cart_service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customSwaggerConfig() {
        return new OpenAPI().info(new Info().title("Cart-Service").version("1.0").description("API documentation for cart service"));
    }
}
