package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.article.NaverNewsResponse;
import com.example.monewteam08.dto.response.article.item.NaverNewsItem;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ArticleFetchServiceImpl implements ArticleFetchService {

  private final RestTemplate restTemplate;
  private String naverClientId;
  private String naverClientSecret;

  public ArticleFetchServiceImpl(RestTemplateBuilder builder,
      @Value("${naver.api.client-id}") String naverClientId,
      @Value("${naver.api.client-secret}") String naverClientSecret) {
    this.restTemplate = builder.build();
    this.naverClientId = naverClientId;
    this.naverClientSecret = naverClientSecret;
  }

  @Override
  public List<NaverNewsItem> fetchNaverArticles() {

    String uri = UriComponentsBuilder
        .fromHttpUrl("https://openapi.naver.com/v1/search/news.json")
        .queryParam("query", "뉴스")
        .queryParam("display", 10)
        .queryParam("start", 1)
        .queryParam("sort", "date")
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Naver-Client-Id", naverClientId);
    headers.set("X-Naver-Client-Secret", naverClientSecret);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<NaverNewsResponse> response = restTemplate.exchange(
        uri,
        HttpMethod.GET,
        entity,
        NaverNewsResponse.class
    );

    return response.getBody().items();
  }
}



