package com.example.monewteam08.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final AuthenticatedUserInterceptor authenticatedUserInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authenticatedUserInterceptor)
        .addPathPatterns("/api/**")
        .excludePathPatterns("/api/users/**", "/api/comments", "/api/interests");
  }
}
