package com.bedatasolutions.leaseDrop.config.app;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
//@SecurityScheme(name = "LeaseDrop-sec", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
//@SecurityScheme(name = "LeaseDrop-sec", type = SecuritySchemeType.HTTP, scheme = "basic", in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "LeaseDrop - Dev", version = "v3", description = "Develop RestFull spring boot application"))
public class OpenApi3Config {
    @Bean
    public OpenAPI openApiSpec() {
        return new OpenAPI().components(new Components()
                .addSchemas("ApiErrorResponse", new ObjectSchema()
                        .addProperty("status", new IntegerSchema())
                        .addProperty("code", new StringSchema())
                        .addProperty("message", new StringSchema())
                        .addProperty("fieldErrors", new ArraySchema().items(
                                new Schema<ArraySchema>().$ref("ApiFieldError"))))
                .addSchemas("ApiFieldError", new ObjectSchema()
                        .addProperty("code", new StringSchema())
                        .addProperty("message", new StringSchema())
                        .addProperty("property", new StringSchema())
                        .addProperty("rejectedValue", new ObjectSchema())
                        .addProperty("path", new StringSchema())));
    }


    @Bean
    public OperationCustomizer operationCustomizer() {
        // add error type to each operation
        return (operation, handlerMethod) -> {
            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                    .description("Error")
                    .content(new Content().addMediaType("*/*", new MediaType().schema(
                            new Schema<MediaType>().$ref("ApiErrorResponse")))));
            return operation;
        };
    }

    @Bean
    public OpenApiCustomizer sortTagsAlphabetically() {
        // TODO: Sort by the api indexing name
        return this::extractedOpenApiTagOrder;
    }

    private void extractedOpenApiTagOrder(OpenAPI openApi) {
        final Pattern p = Pattern.compile("^\\d+");
        Comparator<Tag> c = new Comparator<Tag>() {
            @Override
            public int compare(Tag objectE1, Tag objectE2) {
                String object1 = objectE1.getName(), object2 = objectE2.getName();
                Matcher m = p.matcher(object1);
                Integer number1 = null;
                if (!m.find()) {
                    return object1.compareTo(object2);
                } else {
                    Integer number2 = null;
                    number1 = Integer.parseInt(m.group());
                    m = p.matcher(object2);
                    if (!m.find()) {
                        return object1.compareTo(object2);
                    } else {
                        number2 = Integer.parseInt(m.group());
                        int comparison = number1.compareTo(number2);
                        if (comparison != 0) {
                            return comparison;
                        } else {
                            return object1.compareTo(object2);
                        }
                    }
                }
            }
        };
        openApi.setTags(openApi.getTags()
                .stream()
                .sorted(c)
                .toList());
    }
}
