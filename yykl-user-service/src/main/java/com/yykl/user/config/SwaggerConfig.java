package com.yykl.user.config;

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
    public OpenAPI customOpenAPI() {
        // 添加安全需求，指定使用"BearerAuth"的安全方案
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");
        return new OpenAPI()
                .info(new Info().title("你的项目API文档").version("1.0"))
                .addSecurityItem(securityRequirement)
                .components(new Components()
                        // 定义"BearerAuth"安全方案，类型为HTTP，方案为bearer，格式为JWT
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }
}