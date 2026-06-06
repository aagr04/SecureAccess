package com.empresa.loginapp.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("LoginApp API").version("1.0.0")
                .description("Backend hexagonal con JWT y PostgreSQL. El registro de usuarios no requiere email: el backend genera el correo con dominio @mail.com. El login acepta username o correo generado. Los endpoints protegidos requieren JWT, los endpoints administrativos requieren rol ADMIN, /api/usuarios/me permite perfil propio y la carga masiva acepta .xlsx, .xls o .csv sin columna email."));
    }
}
