package com.ewecarreira.library.configurations;

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
                .info(new Info()
                        .title("Library API")
                        .description("API do projeto de controle de aluguel de livros.")
                        .version("1.0.0")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                        .contact(contact()));
    }

    private Contact contact() {
        Contact contact = new Contact();
        contact.setName("Ewerton Carreira");
        contact.setUrl("http://github.com/carreiras");
        contact.setEmail("ewertoncarreira@gmail.com");
        return contact;
    }
}
