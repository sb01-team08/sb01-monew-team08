package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

  @Mock
  private ArticleRepository articleRepository;

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

  @Test
  void 기사_목록_조회_성공() {
    // given
    UUID articleId = UUID.randomUUID();
    ArticleDto dto = new ArticleDto(
        articleId,
        "NAVER",
        "https://naver.com/article/123",
        "테스트 제목",
        LocalDateTime.parse("2025-04-22T02:20:37.781"),
        "요약 내용",
        100L,
        true
    );
    CursorPageResponseArticleDto response = new CursorPageResponseArticleDto(
        List.of(dto),
        "next-cursor",
        LocalDateTime.parse("2025-04-06T15:04:05"),
        10,
        100,
        true
    );

    given(articleService.getArticles()).willReturn(response);

    // when
    CursorPageResponseArticleDto result = articleService.getArticles();

    // then
    assertThat(result.articles()).hasSize(1);
    assertThat(result.totalElements()).isEqualTo(100);
    assertThat(result.hasNext()).isTrue();
    assertThat(result.articles().get(0).viewedByMe()).isTrue();
  }

  @Test
  void 기사_논리_삭제_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();
    Constructor<Article> constructor = Article.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Article article = constructor.newInstance();
    ReflectionTestUtils.setField(article, "id", articleId);
    ReflectionTestUtils.setField(article, "isActive", false);

    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

    // when
    articleService.softDelete(articleId);

    // then
    assertThat(article.isActive()).isFalse();
    verify(articleRepository).save(article);
  }

  @Test
  void 기사_물리_삭제_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();
    Constructor<Article> constructor = Article.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Article article = constructor.newInstance();
    ReflectionTestUtils.setField(article, "id", articleId);

    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

    // when
    articleService.hardDelete(articleId);

    // then
    verify(articleRepository).delete(article);
  }
}
