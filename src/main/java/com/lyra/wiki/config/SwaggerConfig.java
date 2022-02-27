package com.lyra.wiki.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("lyra知识库系统 API")
                        .description("lyra知识库系统")
                        .version("v0.0.1")
                        )
                .externalDocs(new ExternalDocumentation()
                        .description("lyra知识库系统")
                        .url("https://www.cnblogs.com/lyraHeartstrings/"));
    }
}
