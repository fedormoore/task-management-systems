package ru.moore.task_management_systems.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Task Management Systems API",
                description = "Task Management Systems", version = "1.0.0",
                contact = @Contact(
                        name = "Fedor Moore",
                        email = "fedormoore@gmail.com"
                )
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer",
        in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {


}
