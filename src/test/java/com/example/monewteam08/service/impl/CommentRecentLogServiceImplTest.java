package com.example.monewteam08.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentRecentLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.mapper.CommentRecentLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRecentLogRepository;
import com.example.monewteam08.repository.CommentRepository;
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
class CommentRecentLogServiceImplTest {

  @InjectMocks
  private CommentRecentLogServiceImpl commentRecentLogService;

  @Mock
  private UserActivityLogRepository userActivityLogRepository;
  @Mock
  private CommentRecentLogRepository commentRecentLogRepository;
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private CommentRecentLogMapper commentRecentLogMapper;

  private UUID userId;
  private UUID commentId;
  private UUID articleId;

  private UserActivityLog userActivityLog;
  private User user;
  private Article article;
  private Comment comment;
  private CommentRecentLog commentRecentLog;

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
        LocalDateTime.now(), null);
    ReflectionTestUtils.setField(article, "id", articleId);
    comment = new Comment(articleId, userId, "testComment");
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.now());

    commentRecentLog = CommentRecentLog.builder()
        .activityLog(userActivityLog)
        .comment(comment)
        .articleId(articleId)
        .articleTitle(article.getTitle())
        .user(user)
        .commentContent(comment.getContent())
        .commentCreatedAt(comment.getCreatedAt())
        .build();

  }

  @Test
  @DisplayName("최신 작성한 댓글 로그를 성공적으로 추가한다.")
  void createCommentRecentLog() {
    // given
    given(userActivityLogRepository.findByUserId(userId)).willReturn(Optional.of(userActivityLog));
    given(articleRepository.findById(comment.getArticleId())).willReturn(Optional.of(article));
    given(commentRecentLogMapper.toEntity(userActivityLog, comment, article.getTitle())).willReturn(
        commentRecentLog);

    // when
    commentRecentLogService.addCommentRecentLog(userId, comment);

    // then
    verify(commentRecentLogRepository).save(any(CommentRecentLog.class));
  }

  @Test
  @DisplayName("댓글 삭제 시 해당 로그도 삭제한다.")
  void removeCommentRecentLog() {
    // given
    willDoNothing().given(commentRecentLogRepository)
        .deleteCommentRecentLogByCommentIdAndUserId(userId, commentId);

    // when
    commentRecentLogService.removeCommentRecentLog(userId, commentId);

    // then
    verify(commentRecentLogRepository).deleteCommentRecentLogByCommentIdAndUserId(userId,
        commentId);
  }

  @Test
  @DisplayName("최신 작성 댓글 로그를 성공적으로 가져온다.")
  void getCommentRecentLogs() {
    // given
    List<CommentRecentLog> logs = IntStream.range(0, 9)
        .mapToObj(i -> CommentRecentLog.builder()
            .activityLog(userActivityLog)
            .comment(comment)
            .articleId(UUID.randomUUID())
            .articleTitle("Article " + i)
            .user(user)
            .commentContent("Comment " + i)
            .commentCreatedAt(LocalDateTime.now().minusDays(i))
            .build())
        .toList();

    given(commentRecentLogRepository.getCommentRecentLogsByActivityLogOrderByCreatedAtDesc(
        eq(userActivityLog), any(PageRequest.class))).willReturn(logs);

    for (CommentRecentLog log : logs) {
      given(commentRecentLogMapper.toResponse(eq(log)))
          .willReturn(CommentRecentLogResponse.builder()
              .id(log.getId())
              .articleId(log.getArticleId())
              .articleTitle(log.getArticleTitle())
              .userId(log.getUser().getId())
              .userNickname(userActivityLog.getUser().getNickname())
              .content(log.getCommentContent())
              .likeCount(comment.getLikeCount())
              .createdAt(log.getCreatedAt())
              .build());
    }

    // when
    List<CommentRecentLogResponse> responses = commentRecentLogService.getCommentRecentLogs(
        userActivityLog);

    // then
    Assertions.assertThat(responses).isNotEmpty();
    Assertions.assertThat(responses).hasSize(9);
  }

}