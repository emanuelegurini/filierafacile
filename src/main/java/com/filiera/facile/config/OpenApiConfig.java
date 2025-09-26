package com.filiera.facile.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Filiera Facile API")
                        .description("API per la gestione di eventi e pacchetti prodotti nella filiera agricola")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Filiera Facile Team")
                        )
                );
    }
}