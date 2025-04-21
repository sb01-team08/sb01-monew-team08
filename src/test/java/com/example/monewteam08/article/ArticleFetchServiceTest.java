package com.example.monewteam08.article;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.monewteam08.dto.response.article.item.NaverNewsItem;
import com.example.monewteam08.service.impl.ArticleFetchServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;

@SpringBootTest
public class ArticleFetchServiceTest {

  @Value("${naver.api.client-id}")
  private String clientId;

  @Value("${naver.api.client-secret}")
  private String clientSecret;

  @Autowired
  private RestTemplateBuilder builder;

  private ArticleFetchServiceImpl articleFetchService;

  @BeforeEach
  public void setUp() {
    articleFetchService = new ArticleFetchServiceImpl(builder, clientId, clientSecret);
  }

  @Test
  void 네이버_기사_가져오기() {
    // given

    // when
    List<NaverNewsItem> articles = articleFetchService.fetchNaverArticles();
    articles.forEach(System.out::println);
    // then
    assertThat(articles).isNotNull();
  }
}
