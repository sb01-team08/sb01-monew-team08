package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.NewsViewMLog;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.mapper.NewsViewMLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class NewsViewMLogServiceImplTest {

  @InjectMocks
  private NewsViewMLogServiceImpl newsViewMLogService;

  @Mock
  private UserActivityMongoRepository userActivityMongoRepository;
  @Mock
  private ArticleRepository articleRepository;
  @Mock
  private NewsViewMLogMapper newsViewMLogMapper;
  @Mock
  private CommentRepository commentRepository;

  @Test
  @DisplayName("뉴스 조회 로그 추가 - 10개 이하")
  void addArticleView_success_under10() {
    // given
    UUID userId = UUID.randomUUID();
    UUID articleId = UUID.randomUUID();

    Article article = new Article("source", "title", "summary", "sourceUrl", LocalDateTime.now(),
        null);
    ReflectionTestUtils.setField(article, "id", articleId);
    NewsViewMLog log = NewsViewMLog.builder().articleId(articleId).viewedBy(userId).build();

    UserActivity userActivity = UserActivity.builder()
        .id(userId)
        .articleViews(new ArrayList<>())
        .build();

    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));
    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
    given(newsViewMLogMapper.toEntity(article, userId)).willReturn(log);

    // when
    newsViewMLogService.addArticleView(userId, articleId);

    // then
    then(userActivityMongoRepository).should().save(argThat(saved -> {
      List<NewsViewMLog> logs = saved.getArticleViews();
      return logs.size() == 1 && logs.get(0).getArticleId().equals(articleId);
    }));
  }

  @Test
  @DisplayName("뉴스 조회 로그 추가 - 10개 초과")
  void addArticleView_success_over10() {
    // given
    UUID userId = UUID.randomUUID();
    UUID articleId = UUID.randomUUID();

    List<NewsViewMLog> existing = IntStream.range(0, 10)
        .mapToObj(i -> NewsViewMLog.builder()
            .articleId(UUID.randomUUID())
            .viewedBy(userId)
            .build())
        .collect(Collectors.toList());

    Article article = new Article("source", "title", "summary", "sourceUrl", LocalDateTime.now(),
        null);
    ReflectionTestUtils.setField(article, "id", articleId);

    NewsViewMLog newLog = NewsViewMLog.builder().articleId(articleId).viewedBy(userId).build();

    UserActivity userActivity = UserActivity.builder()
        .id(userId)
        .articleViews(new ArrayList<>(existing))
        .build();

    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));
    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
    given(newsViewMLogMapper.toEntity(article, userId)).willReturn(newLog);

    // when
    newsViewMLogService.addArticleView(userId, articleId);

    // then
    then(userActivityMongoRepository).should().save(argThat(saved -> {
      List<NewsViewMLog> logs = saved.getArticleViews();
      return logs.size() == 10 &&
          logs.get(0).getArticleId().equals(articleId); // new log is first
    }));
  }

  @Test
  @DisplayName("뉴스 조회 로그 리스트 조회 성공")
  void getNewsViewLogs_success() {
    // given
    UUID articleId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    NewsViewMLog log = NewsViewMLog.builder()
        .articleId(articleId)
        .viewedBy(userId)
        .build();

    Article article = new Article("source", "title", "summary", "sourceUrl", LocalDateTime.now(),
        null);
    ReflectionTestUtils.setField(article, "id", articleId);

    List<Object[]> commentCounts = new ArrayList<>();
    commentCounts.add(new Object[]{articleId, 5L});

    given(commentRepository.countGroupByArticleIds(Set.of(articleId)))
        .willReturn(commentCounts);

    given(articleRepository.findAllById(Set.of(articleId)))
        .willReturn(List.of(article));

    NewsViewLogResponse response = new NewsViewLogResponse(
        UUID.randomUUID(), userId, LocalDateTime.now(),
        articleId, "naver", "url", "title", LocalDateTime.now(),
        "summary", 5, 0
    );

    given(newsViewMLogMapper.toResponse(any(), anyInt(), anyInt()))
        .willReturn(response);

    // when
    List<NewsViewLogResponse> result = newsViewMLogService.getNewsViewLogs(List.of(log));

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(response);

    verify(commentRepository).countGroupByArticleIds(Set.of(articleId));
    verify(articleRepository).findAllById(Set.of(articleId));
    verify(newsViewMLogMapper).toResponse(log, 5, 0);
  }

}