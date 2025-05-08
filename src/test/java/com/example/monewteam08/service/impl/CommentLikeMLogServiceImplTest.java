package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.CommentLikeMLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.mapper.CommentLikeMLogMapper;
import com.example.monewteam08.mapper.CommentMLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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
class CommentLikeMLogServiceImplTest {

  @InjectMocks
  private CommentLikeMLogServiceImpl commentLikeMLogService;

  @Mock
  private UserActivityMongoRepository userActivityMongoRepository;

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private CommentLikeMLogMapper commentLikeMLogMapper;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CommentRepository commentRepository;
  @Mock
  private CommentMLogMapper commentMLogMapper;

  private UUID userId;
  private String userNickname;
  private UUID commentId;
  private UUID articleId;

  private Article article;
  private Comment comment;
  private CommentLike commentLike;
  private CommentLikeMLog commentLikeMLog;

  private UserActivity userActivity;

  private CommentLikeLogResponse response;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    commentId = UUID.randomUUID();
    articleId = UUID.randomUUID();
    userNickname = "tester";

    comment = new Comment(articleId, userId, "댓글 내용");
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "likeCount", 10);

    article = new Article("source", "title", "summary", "sourceUrl", LocalDateTime.now(), null);
    ReflectionTestUtils.setField(article, "id", articleId);

    commentLike = new CommentLike(userId, commentId);
    ReflectionTestUtils.setField(commentLike, "id", UUID.randomUUID());

    commentLikeMLog = CommentLikeMLog.builder()
        .commentId(commentId)
        .articleId(articleId)
        .commentUserId(userId)
        .commentCreatedAt(LocalDateTime.now())
        .build();

    userActivity = UserActivity.builder()
        .id(userId)
        .nickname(userNickname)
        .email("test@test.com")
        .commentLikes(new ArrayList<>())
        .build();

  }

  @Test
  @DisplayName("댓글 좋아요 로그를 성공적으로 추가한다.")
  void addCommentLike_success() {
    // given
    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));
    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
    given(commentLikeMLogMapper.toEntity(commentLike, comment, article, userId))
        .willReturn(commentLikeMLog);

    // when
    commentLikeMLogService.addCommentLikeLog(userId, userNickname, commentLike, comment);

    // then
    then(userActivityMongoRepository).should().save(argThat(saved -> {
      List<CommentLikeMLog> commentLikes = saved.getCommentLikes();
      return commentLikes.size() == 1 && commentLikes.get(0).getCommentId().equals(commentId);
    }));
  }

  @Test
  @DisplayName("댓글 좋아요 로그를 성공적으로 조회한다.")
  void getCommentLikeLogs_success() {
    // given
    int likeCount = 10;

    Comment comment = new Comment(articleId, userId, "댓글 내용");
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "likeCount", likeCount);

    User user = new User("test@test.com", "tester", "!test1234");
    ReflectionTestUtils.setField(user, "id", userId);
    ReflectionTestUtils.setField(user, "nickname", "tester");

    given(commentRepository.findAllById(Set.of(commentId))).willReturn(List.of(comment));
    given(userRepository.findAllById(Set.of(userId))).willReturn(List.of(user));

    response = CommentLikeLogResponse.builder()
        .id(commentLikeMLog.getId())
        .createdAt(commentLikeMLog.getCommentCreatedAt())
        .commentId(commentId)
        .articleId(articleId)
        .articleTitle(article.getTitle())
        .commentUserId(userId)
        .commentUserNickname("tester")
        .commentContent("댓글 내용")
        .commentCreatedAt(commentLikeMLog.getCommentCreatedAt())
        .commentLikeCount(likeCount)
        .build();

    given(commentLikeMLogMapper.toResponse(commentLikeMLog, likeCount, "tester", "댓글 내용"))
        .willReturn(response);

    // when
    List<CommentLikeLogResponse> result = commentLikeMLogService.getCommentLikeLogs(
        List.of(commentLikeMLog));

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(response);

    verify(commentRepository).findAllById(Set.of(commentId));
    verify(userRepository).findAllById(Set.of(userId));
    verify(commentLikeMLogMapper).toResponse(commentLikeMLog, likeCount, "tester", "댓글 내용");
  }

  @Test
  @DisplayName("댓글 좋아요 로그 삭제")
  void deleteCommentLikeLog_success() {
    // given
    CommentLikeMLog target = CommentLikeMLog.builder()
        .commentId(commentId)
        .commentUserId(userId)
        .build();

    userActivity.updateCommentLikes(new ArrayList<>(List.of(target)));

    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));

    // when
    commentLikeMLogService.deleteCommentLikeLog(userId, commentId);

    // then
    then(userActivityMongoRepository).should().save(argThat(saved ->
        saved.getCommentLikes().isEmpty()
    ));
  }

}