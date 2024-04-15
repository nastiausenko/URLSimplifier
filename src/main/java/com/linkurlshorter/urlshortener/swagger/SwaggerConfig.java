package com.linkurlshorter.urlshortener.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 *
 * <p>
 * This class defines the configuration for Swagger/OpenAPI documentation.
 * It provides information about the API such as title, description, and version.
 * Additionally, it specifies the security scheme (JWT) to be used for authentication.
 * </p>
 *
 * @author Anastasiia Usenko
 */

@OpenAPIDefinition(
        info = @Info(
                title="URL shortener",
                description = "API URL shortener",
                version = "1.0.0"
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

}