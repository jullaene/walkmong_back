package org.jullaene.walkmong_back.config;


import static org.springframework.security.config.Elements.JWT;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Components components = new Components().addSecuritySchemes(JWT, getJwtSecurityScheme());
        SecurityRequirement securityItem = new SecurityRequirement().addList(JWT);

        Info info = new Info()
                .title("줄래네 워크멍 API Document")
                .version("v0.0.1")
                .description("API 명세서입니다.");

        return new OpenAPI()
                .components(components)
                .info(info)
                .addSecurityItem(securityItem);
    }
    private SecurityScheme getJwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }
}

