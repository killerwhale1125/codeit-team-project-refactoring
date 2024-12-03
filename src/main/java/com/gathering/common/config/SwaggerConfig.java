package com.gathering.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String jwt = "JWT";
    private static final String scheme = "bearer";
    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("jwt");
        Components components = new Components().addSecuritySchemes("jwt", new SecurityScheme()
                .name("jwt")
                .type(SecurityScheme.Type.HTTP)
                .scheme(scheme)
                .bearerFormat(jwt)
        );
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)  // 글로벌 보안 설정
                .components(components);
    }

    private Info apiInfo() {
        return new Info()
                .title("Codeit4")
                .description("codeit-4-swagger")
                .version("1.0.0");
    }
}
