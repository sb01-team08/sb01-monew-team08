package com.example.monewteam08.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.CommentLikeLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.mapper.CommentLikeLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentLikeLogRepository;
import com.example.monewteam08.repository.UserActivityLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
class CommentLikeLogServiceImplTest {

  @InjectMocks
  private CommentLikeLogServiceImpl commentLikeLogService;

  @Mock
  private CommentLikeLogRepository commentLikeLogRepository;
  @Mock
  private UserActivityLogRepository userActivityLogRepository;
  @Mock
  private ArticleRepository articleRepository;
  @Mock
  private CommentLikeLogMapper commentLikeLogMapper;

  private UUID userId;
  private UUID commentId;
  private UUID articleId;
  private UUID commentLikeId;

  private UserActivityLog userActivityLog;
  private User user;
  private Article article;
  private Comment comment;
  private CommentLike commentLike;
  private CommentLikeLog commentLikeLog;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    commentId = UUID.randomUUID();
    articleId = UUID.randomUUID();
    commentLikeId = UUID.randomUUID();

    user = new User("test@example.com", "tester", "tester1234!");
    ReflectionTestUtils.setField(user, "id", userId);
    userActivityLog = new UserActivityLog(user);
    ReflectionTestUtils.setField(userActivityLog, "id", userId);
    article = new Article("testurl", "mocktitle", "mocksummary", "sourceUrl",
        LocalDateTime.now(), null);
    ReflectionTestUtils.setField(article, "id", articleId);
    comment = new Comment(articleId, userId, "testComment");
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.now());
    commentLike = new CommentLike(userId, commentId);
    ReflectionTestUtils.setField(commentLike, "id", commentLikeId);

    commentLikeLog = CommentLikeLog.builder()
        .commentId(comment.getId())
        .articleId(article.getId())
        .articleTitle(article.getTitle())
        .commentUserId(comment.getUserId())
        .commentUserNickname(user.getNickname())
        .commentContent(comment.getContent())
        .commentCreatedAt(LocalDateTime.now())
        .activityLog(userActivityLog)
        .build();
  }

  @Test
  @DisplayName("댓글 좋아요 로그를 성공적으로 추가한다.")
  void addCommentLikeLog() {
    // given
    int countLogs = 1;
    given(userActivityLogRepository.findByUserId(userId)).willReturn(Optional.of(userActivityLog));
    given(articleRepository.findById(comment.getArticleId())).willReturn(Optional.of(article));
    given(commentLikeLogMapper.toEntity(userActivityLog, comment, article)).willReturn(
        commentLikeLog);
    given(commentLikeLogRepository.countCommentLikeLogByUserId(userId)).willReturn(countLogs);

    // when
    commentLikeLogService.addCommentLikeLog(userId, commentLike, comment);

    // then
    verify(commentLikeLogRepository, never()).deleteAll(any());
    verify(commentLikeLogRepository).save(any(CommentLikeLog.class));
  }

//  @Test
//  @DisplayName("UserActivityLog를 불러오지 못해 예외를 반환한다.")
//  void failed_addCommentLikeLog_cannotLoadUserActivityLog() {
//    // given
//
//    // when
//
//    // then
//    Assertions.assertThat();
//  }

//  @Test
//  @DisplayName("Article을 불러오지 못해 예외를 반환한다.")
//  void failed_addCommentLikeLog_cannotLoadArticle() {
//    // given
//
//    // when
//
//    // then
//    Assertions.assertThat();
//  }

  @Test
  @DisplayName("댓글 좋아요 로그 기록이 10개 이상이면 해당 개수의 로그를 삭제한다.")
  void deleteExcessCommentLikeLogs() {
    // given
    int limitLogs = 10;
    int countLogs = 12;
    given(commentLikeLogRepository.countCommentLikeLogByUserId(userId)).willReturn(countLogs);

    List<CommentLikeLog> logsToDelete = IntStream.range(0, countLogs - limitLogs)
        .mapToObj(i -> CommentLikeLog.builder()
            .activityLog(userActivityLog)
            .commentId(UUID.randomUUID())
            .articleId(UUID.randomUUID())
            .articleTitle("title" + i)
            .commentContent("content" + i)
            .commentCreatedAt(LocalDateTime.now().minusDays(i + 1))
            .commentUserId(userId)
            .commentUserNickname("tester")
            .build())
        .collect(Collectors.toList());

    given(commentLikeLogRepository.findOldestLogs(userId,
        PageRequest.of(0, countLogs - limitLogs))).willReturn(logsToDelete);

    // when
    commentLikeLogService.deleteExcessCommentLikeLogs(userId);

    // then
    verify(commentLikeLogRepository).findOldestLogs(eq(userId),
        eq(PageRequest.of(0, countLogs - limitLogs)));
    verify(commentLikeLogRepository).deleteAll(logsToDelete);
  }

}