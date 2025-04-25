package com.example.monewteam08.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpaWebConfig implements WebMvcConfigurer {

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
