package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.dto.ArticleInterestCount;
import com.example.monewteam08.dto.FilteredArticleDto;
import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.mapper.ArticleMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.SubscriptionRepository;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import com.example.monewteam08.service.Interface.ArticleViewService;
import com.example.monewteam08.service.Interface.NotificationService;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private InterestRepository interestRepository;

  @Mock
  private ArticleMapper articleMapper;

  @Mock
  private ArticleViewService articleViewService;

  @Mock
  private ArticleFetchService articleFetchService;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private ArticleServiceImpl articleService;

  @Test
  public void 기사_키워드_필터링()
      throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    // given
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    Subscription subscription = new Subscription();
    ReflectionTestUtils.setField(subscription, "userId", userId);
    ReflectionTestUtils.setField(subscription, "interestId", interestId);
    when(subscriptionRepository.findAll()).thenReturn(List.of(subscription));

    Interest interest = new Interest();
    ReflectionTestUtils.setField(interest, "id", interestId);
    ReflectionTestUtils.setField(interest, "name", "경제");
    ReflectionTestUtils.setField(interest, "keywords", List.of("경제", "금융"));

    when(interestRepository.findAllById(List.of(interestId))).thenReturn(List.of(interest));
    when(interestRepository.findById(interestId)).thenReturn(Optional.of(interest));

    Constructor<Article> constructor = Article.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Article article = constructor.newInstance();
    ReflectionTestUtils.setField(article, "title", "오늘의 경제 뉴스");
    ReflectionTestUtils.setField(article, "summary", "금융 시장이 요동치고 있습니다.");
    List<Article> allArticles = List.of(article);

    // when
    FilteredArticleDto result = articleService.filterWithKeywords(allArticles, userId);
    System.out.println(
        "result: " + result.getArticles().get(0).getTitle());

    // then
    assertThat(result.getArticles()).hasSize(1);
    assertThat(result.getArticles().get(0).getTitle()).contains("경제");

    List<ArticleInterestCount> counts = result.getArticleInterestCounts();
    assertThat(counts).hasSize(1);
    assertThat(counts.get(0).interestName()).isEqualTo("경제");
    assertThat(counts.get(0).articleCount()).isEqualTo(1);
  }

  @Test
  void 알림_생성_성공() {
    // given
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    Article article = new Article("NAVER", "오늘의 경제 뉴스", "경제가 어렵습니다", "http://a.com",
        LocalDateTime.now());
    List<Article> articles = List.of(article);
    ArticleInterestCount count = new ArticleInterestCount(interestId, "경제", 1);
    FilteredArticleDto filtered = new FilteredArticleDto(articles, List.of(count));

    given(articleFetchService.fetchAllArticles()).willReturn(articles);
    given(articleRepository.findAll()).willReturn(List.of());
    given(articleService.filterWithKeywords(anyList(), eq(userId))).willReturn(filtered);

    // when
    articleService.fetchAndSave(userId);

    // then
    verify(notificationService).createArticleNotification(eq(userId), eq(interestId), eq("경제"),
        eq(1));
  }

  @Test
  void 기사_불러와서_저장_성공() {
    // given
    UUID userId = UUID.randomUUID();

    Article article1 = new Article("NAVER", "오늘의 경제 뉴스", "경제가 어렵습니다", "http://a.com",
        LocalDateTime.now());
    Article article2 = new Article("NAVER", "중복 기사", "중복 설명", "http://b.com", LocalDateTime.now());

    // 기사
    List<Article> fetchedArticles = List.of(article1, article2);
    given(articleRepository.findAll()).willReturn(
        List.of(new Article("NAVER", "이미 있는 기사", "중복 설명", "http://b.com", LocalDateTime.now()))
    );

    given(articleFetchService.fetchAllArticles()).willReturn(fetchedArticles);
    given(articleRepository.findAll()).willReturn(
        List.of(new Article("NAVER", "이미 있는 기사", "중복 설명", "http://b.com", LocalDateTime.now())));

    // 관심사
    Interest interest = new Interest("경제", List.of("경제", "환율"));
    Subscription subscription = new Subscription(userId, interest.getId());
    given(subscriptionRepository.findAll()).willReturn(List.of(subscription));
    given(interestRepository.findById(interest.getId())).willReturn(Optional.of(interest));

    // when
    articleService.fetchAndSave(userId);

    // then
    verify(articleRepository).saveAll(List.of(article1));
    verify(articleRepository, never()).saveAll(List.of(article2));

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

    given(articleRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(
        articlePage);

    ArticleDto dto = mock(ArticleDto.class);
    given(articleMapper.toDto(any(Article.class), 0, anyBoolean())).willReturn(dto);

    // when
    CursorPageResponseArticleDto result = articleService.getArticles(keyword, interestId, sourceIn,
        publishDateFrom, publishDateTo, orderBy, direction, cursor, after, limit, userId);

    // then
    assertThat(result).isNotNull();
    assertThat(result.content()).hasSize(1);
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
