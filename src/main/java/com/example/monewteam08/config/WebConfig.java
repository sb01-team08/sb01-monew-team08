package com.example.monewteam08.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final AuthenticatedUserInterceptor authenticatedUserInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authenticatedUserInterceptor)
        .addPathPatterns("/api/**")
        .excludePathPatterns("/api/users/**", "/api/comments",
            "/api/interests", "/api/user-activities",
            "/api/articles/restore"); // todo: header 검사 제외하고 싶을 때
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    // 단일 세그먼트 (확장자 없는 요청) → index.html
    registry.addViewController("/{path:[^\\.]+}")
        .setViewName("forward:/");
    // 다중 세그먼트 (확장자 없는 요청) → index.html
    registry.addViewController("/**/{path:[^\\.]+}")
        .setViewName("forward:/");
  }
}
