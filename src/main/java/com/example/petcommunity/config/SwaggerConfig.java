package com.example.petcommunity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Pet Community API")
                        .description("Pet Community application API documentation")
                        .version("v0.0.1")
                        .contact(new Contact()
                                .name("Pet Community Support")
                                .url("http://petcommunity.example.com")
                                .email("support@petcommunity.example.com")));
    }


}