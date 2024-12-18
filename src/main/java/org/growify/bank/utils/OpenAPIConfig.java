package org.growify.bank.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${growify.openapi.dev-url}")
    private String devUrl;

    @Value("${growify.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI getOpenApi() {
        OpenAPI openAPI = new OpenAPI();

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");
        openAPI.setServers(List.of(devServer, prodServer));


        Contact contact = new Contact();
        contact.setEmail("growify@contact.com");
        contact.setName("Growify");
        contact.setUrl("https://www.growify.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://github.com/growify-bank/growify?tab=MIT-1-ov-file");

        Info info = new Info()
                .title("Bank")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for managing bank.")
                .termsOfService("https://www.growify.com/terms").license(mitLicense);

        openAPI.setInfo(info);
        openAPI.components(new Components()
                        .addSecuritySchemes("JWTToken", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")
                        )
                        .addSecuritySchemes("XSRFToken", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .name("X-XSRF-TOKEN")
                                .in(SecurityScheme.In.HEADER)
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("JWTToken")
                        .addList("XSRFToken")
                );
        return openAPI;
    }
}
