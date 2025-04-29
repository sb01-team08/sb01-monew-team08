package com.example.monewteam08.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .version("1.0.0")
        .title("Monew API")
        .description("코드잇 중급 프로젝트 모뉴 API");

    return new OpenAPI().info(info);
  }
}
