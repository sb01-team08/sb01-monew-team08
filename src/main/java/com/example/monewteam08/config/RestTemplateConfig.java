package com.example.monewteam08.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplateBuilder restTemplate() {
    return new RestTemplateBuilder();
  }
}
