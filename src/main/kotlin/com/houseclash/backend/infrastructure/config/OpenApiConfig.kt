package com.houseclash.backend.infrastructure.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("HouseClash API")
                .description("API REST del backend de HouseClash — gestión de casas, tareas, categorías y cartas")
                .version("0.0.1")
        )
        .addSecurityItem(SecurityRequirement().addList("Bearer Token"))
        .components(
            Components().addSecuritySchemes(
                "Bearer Token",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Introduce el token JWT obtenido en /api/users/login")
            )
        )
}

