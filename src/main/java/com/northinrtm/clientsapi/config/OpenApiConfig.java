package com.northinrtm.clientsapi.config;

import com.northinrtm.clientsapi.dto.ErrorResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Clients API"))
                .components(new Components());
    }

    @Bean
    public OpenApiCustomizer globalErrorResponses() {
        return openApi -> {
            Components components = openApi.getComponents();
            if (components == null) {
                components = new Components();
                openApi.setComponents(components);
            }

            if (components.getSchemas() == null || !components.getSchemas().containsKey("ErrorResponse")) {
                ModelConverters.getInstance()
                        .read(ErrorResponse.class)
                        .forEach(components::addSchemas);
            }

            Content errorContent = new Content().addMediaType(
                    org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
            );

            if (openApi.getPaths() == null) return;

            openApi.getPaths().forEach((path, item) -> item.readOperations().forEach(op -> {
                if (op.getResponses() == null) return;

                op.getResponses().putIfAbsent("400", new ApiResponse()
                        .description("Bad Request")
                        .content(errorContent));

                op.getResponses().putIfAbsent("404", new ApiResponse()
                        .description("Not Found")
                        .content(errorContent));

                op.getResponses().putIfAbsent("409", new ApiResponse()
                        .description("Conflict")
                        .content(errorContent));

                op.getResponses().putIfAbsent("500", new ApiResponse()
                        .description("Internal Server Error")
                        .content(errorContent));
            }));
        };
    }
}
