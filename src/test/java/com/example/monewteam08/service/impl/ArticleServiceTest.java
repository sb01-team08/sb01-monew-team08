package com.example.monewteam08.service.impl;

import com.example.monewteam08.entity.Article;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

  @InjectMocks
  private ArticleServiceImpl articleService;

  @Test
  public void 기사_키워드_필터링() {
    // given
    List<Article> articles = List.of(
        new Article("NAVER", "오늘의 경제 뉴스", "경제가 어렵습니다", "http://a.com", LocalDateTime.now()),
        new Article("NAVER", "오늘의 연예 뉴스", "연예계 소식입니다", "http://b.com", LocalDateTime.now()),
        new Article("NAVER", "경제 회복 중", "경제 성장률이 증가 중", "http://c.com", LocalDateTime.now())
    );

    // when
    List<Article> filteredArticles = articleService.filterWithKeywords(articles);

    // then
    assert (filteredArticles.size() == 2);
  }
}
