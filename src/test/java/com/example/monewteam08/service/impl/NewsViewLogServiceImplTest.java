package com.example.monewteam08.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.NewsViewLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.mapper.NewsViewLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.NewsViewLogRepository;
import com.example.monewteam08.repository.UserActivityLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NewsViewLogServiceImplTest {

  @InjectMocks
  private NewsViewLogServiceImpl newsViewLogService;

  @Mock
  private UserActivityLogRepository userActivityLogRepository;
  @Mock
  private NewsViewLogRepository newsViewLogRepository;
  @Mock
  private ArticleRepository articleRepository;
  @Mock
  private CommentRepository commentRepository;

  @Mock
  private NewsViewLogMapper newsViewLogMapper;

  private UUID userId;
  private UUID commentId;
  private UUID articleId;

  private UserActivityLog userActivityLog;
  private User user;
  private Article article;
  private NewsViewLog newsViewLog;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    commentId = UUID.randomUUID();
    articleId = UUID.randomUUID();

    user = new User("test@example.com", "tester", "tester1234!");
    ReflectionTestUtils.setField(user, "id", userId);
    userActivityLog = new UserActivityLog(user);
    ReflectionTestUtils.setField(userActivityLog, "id", userId);
    article = new Article("testurl", "mocktitle", "mocksummary", "sourceUrl",
        LocalDateTime.now());
    ReflectionTestUtils.setField(article, "id", articleId);

    newsViewLog = NewsViewLog.builder()
        .activityLog(userActivityLog)
        .viewedBy(userId)
        .articleId(article.getId())
        .articleSummary(article.getSummary())
        .articlePublishedDate(article.getPublishDate())
        .articleTitle(article.getTitle())
        .source(article.getSource())
        .sourceUrl(article.getSourceUrl())
        .build();

  }

  @Test
  @DisplayName("뉴스 조회 로그를 추가한다")
  void addNewsViewLog() {
    // given
    given(userActivityLogRepository.findByUserId(userId)).willReturn(Optional.of(userActivityLog));
    given(newsViewLogMapper.toEntity(userActivityLog, article, userId)).willReturn(newsViewLog);

    // when
    newsViewLogService.addNewsViewLog(userId, article);

    // then
    verify(newsViewLogRepository).save(any(NewsViewLog.class));
  }

  @Test
  @DisplayName("뉴스 조회 로그를 삭제한다")
  void deleteNewsViewLog() {
    // given
    willDoNothing().given(newsViewLogRepository)
        .deleteNewsViewLogByCommentIdAndUserId(userId, articleId);

    // when
    newsViewLogService.removeNewsViewLog(userId, articleId);

    // then
    verify(newsViewLogRepository).deleteNewsViewLogByCommentIdAndUserId(userId,
        articleId);

  }

  @Test
  @DisplayName("최신 뉴스 조회 로그를 성공적으로 가져온다.")
  void getNewsViewLogs() {
    // given
    int commentCount = 111;

    List<NewsViewLog> logs = IntStream.range(0, 9)
        .mapToObj(i -> NewsViewLog.builder()
            .activityLog(userActivityLog)
            .viewedBy(userId)
            .articleId(UUID.randomUUID())
            .articleSummary("Summary " + i)
            .articlePublishedDate(LocalDateTime.now().minusDays(i))
            .articleTitle("Article " + i)
            .source("Source " + i)
            .sourceUrl("Source URL " + i)
            .build())
        .toList();

    given(newsViewLogRepository.getNewsViewLogsByActivityLogOrderByCreatedAtDesc(
        eq(userActivityLog), any(PageRequest.class))).willReturn(logs);

    for (NewsViewLog log : logs) {
      given(articleRepository.findById(log.getArticleId()))
          .willReturn(Optional.of(article));
      given(commentRepository.countByArticleId(log.getArticleId())).willReturn(commentCount);

      given(newsViewLogMapper.toResponse(eq(log), eq(commentCount), anyInt()))
          .willReturn(NewsViewLogResponse.builder()
              .id(log.getId())
              .viewedBy(log.getViewedBy())
              .createdAt(log.getCreatedAt())
              .articleId(log.getArticleId())
              .source(log.getSource())
              .sourceUrl(log.getSourceUrl())
              .articleTitle(log.getArticleTitle())
              .articlePublishedDate(log.getArticlePublishedDate())
              .articleSummary(log.getArticleSummary())
              .articleCommentCount(commentCount)
              .articleViewCount(10)
              .build());
    }

    // when
    List<NewsViewLogResponse> responses = newsViewLogService.getNewsViewLogs(userActivityLog);

    // then
    Assertions.assertThat(responses).isNotEmpty();
    Assertions.assertThat(responses).hasSize(9);
  }


}