package com.example.monewteam08.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.monewteam08.entity.Article;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;

@SpringBootTest
class ArticleFetchServiceTest {

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
    List<Article> articles = articleFetchService.fetchNaverArticles();
    articles.forEach(System.out::println);
    // then
    assertThat(articles).isNotNull();
  }

  @Test
  void RSS_기사_가져오기() {
    // given
    String yonhapUrl = "https://www.yonhapnewstv.co.kr/browse/feed/";
    String chosunUrl = "https://www.chosun.com/arc/outboundfeeds/rss/?outputType=xml";
    String hankyungUrl = "https://www.hankyung.com/feed/all-news";

    // when
    List<Article> yonhapArticles = articleFetchService.fetchRssArticles("YONHAP", yonhapUrl);
    yonhapArticles.forEach(
        article -> System.out.println(article.getSource() + " : " + article.getTitle()));
    List<Article> chosunArticles = articleFetchService.fetchRssArticles("CHOSUN", chosunUrl);
    chosunArticles.forEach(
        article -> System.out.println(article.getSource() + " : " + article.getTitle()));
    List<Article> hankyungArticles = articleFetchService.fetchRssArticles("HANKYUNG", hankyungUrl);
    hankyungArticles.forEach(
        article -> System.out.println(article.getSource() + " : " + article.getTitle()));

    // then
    assertThat(yonhapArticles).isNotNull();
    assertThat(chosunArticles).isNotNull();
    assertThat(hankyungArticles).isNotNull();
  }

  @Test
  void 모든_기사_가져오기() {
    // given

    // when
    List<Article> articles = articleFetchService.fetchAllArticles();
    articles.forEach(
        article -> System.out.println(article.getSource() + " : " + article.getTitle()));
    // then
    assertThat(articles).isNotNull();
  }
}
