package com.brokerapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger / OpenAPI belgelendirme yapılandırması
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI yapılandırmasını oluşturur
     *
     * @return OpenAPI nesnesi
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Broker Uygulaması API")
                        .description("Broker uygulaması için RESTful API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Broker App Team")
                                .email("admin@brokerapp.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createSecurityScheme()));
    }

    /**
     * JWT güvenlik şeması oluşturur
     *
     * @return Security scheme nesnesi
     */
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("Bearer Authentication")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
