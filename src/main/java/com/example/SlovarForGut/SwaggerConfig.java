package com.example.SlovarForGut;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Словарей (SlovarForGut)")
                        .version("1.0.0")
                        .description("Документация к REST API для управления словарями на Spring Boot."));
    }
}
