package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.mapper.ArticleMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private ArticleMapper articleMapper;

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
    String keyword = "경제";
    UUID interestId = UUID.randomUUID();
    List<String> sourceIn = List.of("NAVER");
    LocalDateTime publishDateFrom = LocalDateTime.now().minusDays(7);
    LocalDateTime publishDateTo = LocalDateTime.now();
    String orderBy = "publishDate";
    String direction = "DESC";
    String cursor = null;
    LocalDateTime after = null;
    int limit = 10;
    UUID userId = UUID.randomUUID();

    Article article = mock(Article.class);
    Page<Article> articlePage = new PageImpl<>(List.of(article));

    given(articleRepository.findAllByIsActiveTrue(any(Specification.class), any(Pageable.class)))
        .willReturn(articlePage);

    ArticleDto dto = mock(ArticleDto.class);
    given(articleMapper.toDto(any(Article.class), anyBoolean())).willReturn(dto);

    // when
    CursorPageResponseArticleDto result = articleService.getArticles(keyword, interestId, sourceIn,
        publishDateFrom, publishDateTo, orderBy, direction, cursor, after, limit, userId);

    // then
    assertThat(result).isNotNull();
    assertThat(result.articles()).hasSize(1);
  }

  @Test
  void 기사_논리_삭제_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();
    Constructor<Article> constructor = Article.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Article article = constructor.newInstance();
    ReflectionTestUtils.setField(article, "id", articleId);
    ReflectionTestUtils.setField(article, "isActive", true);

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
