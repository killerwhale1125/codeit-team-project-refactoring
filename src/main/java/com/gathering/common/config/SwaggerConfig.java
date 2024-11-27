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
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("Codeit4") // API의 제목
                .description("codeit-4-swagger") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
