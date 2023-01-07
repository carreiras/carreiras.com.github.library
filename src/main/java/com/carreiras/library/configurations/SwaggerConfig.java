package com.carreiras.library.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(appInfo());
    }

    private Info appInfo() {
        return new Info()
                .title("Library")
                .description("Example of microservice for TDD study.")
                .version("1.0")
                .termsOfService("http://swagger.io/terms/")
                .license(appLicense())
                .contact(appContact());
    }

    private License appLicense() {
        return new License()
                .name("Apache 2.0")
                .url("http://springdoc.org");
    }

    private Contact appContact() {
        return new Contact()
                .name("Ewerton Carreira")
                .url("https://github.com/carreiras")
                .email("ewertoncarreira@gmail.com");
    }
}
