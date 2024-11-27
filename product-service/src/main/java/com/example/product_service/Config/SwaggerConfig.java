package com.example.product_service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI customConfig(){
        return new OpenAPI().info(new Info().title("Product App").version("1.0").description("API documentation for product service"));
    }
}
