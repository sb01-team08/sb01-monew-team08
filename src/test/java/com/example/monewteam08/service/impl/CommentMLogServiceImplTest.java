package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentMLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.mapper.CommentMLogMapper;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
class CommentMLogServiceImplTest {

  @Mock
  private UserActivityMongoRepository userActivityMongoRepository;

  @Mock
  private CommentMLogMapper commentMLogMapper;

  @InjectMocks
  private CommentMLogServiceImpl commentMLogService;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private UserRepository userRepository;

  private User user;
  private Comment comment;
  private Article article;
  private CommentMLog commentMLog;
  private UserActivity userActivity;
  private CommentRecentLogResponse response;

  private UUID commentId;

  @BeforeEach
  void setUp() {
    user = new User("test@example.com", "tester", "!test1234");
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());

    article = new Article("testurl", "mocktitle", "mocksummary", "sourceUrl",
        LocalDateTime.now(), null);
    ReflectionTestUtils.setField(article, "id", UUID.randomUUID());

    commentId = UUID.randomUUID();
    comment = new Comment(UUID.randomUUID(), user.getId(), "테스트 댓글");
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "likeCount", 1);

    commentMLog = CommentMLog.builder()
        .id(comment.getId())
        .userId(user.getId())
        .articleId(comment.getArticleId())
        .articleTitle("테스트 제목")
        .build();
    ReflectionTestUtils.setField(commentMLog, "id", commentId); // 반드시 일치해야 함

    response = CommentRecentLogResponse.builder()
        .id(commentMLog.getId())
        .content(comment.getContent())
        .likeCount(10)
        .userNickname(user.getNickname())
        .articleId(UUID.randomUUID())
        .articleTitle("테스트 제목")
        .build();

    userActivity = UserActivity.builder().id(user.getId()).nickname(user.getNickname())
        .email(user.getEmail()).build();
    userActivity.updateComments(new ArrayList<>());
  }

  @Test
  @DisplayName("댓글 로그 추가 성공 - 10개 이하")
  void addRecentComment_success_under_10() {
    // given
    UUID userId = user.getId();
    UUID commentId = comment.getId();
    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));
    given(commentMLogMapper.toEntity(comment, "테스트 제목", user)).willReturn(commentMLog);

    // when
    commentMLogService.addRecentComment(user, comment, "테스트 제목");

    // then
    then(userActivityMongoRepository).should().save(argThat(saved -> {
      List<CommentMLog> comments = saved.getComments();
      return comments.size() == 1 && comments.get(0).getId().equals(commentMLog.getId());
    }));

  }

  @Test
  @DisplayName("댓글 로그 추가 성공 - 10개 이상")
  void addRecentComment_success_over_10() {
    // given
    UUID userId = user.getId();

    List<CommentMLog> existingComments = IntStream.range(0, 10)
        .mapToObj(i -> {
          CommentMLog log = CommentMLog.builder()
              .id(UUID.randomUUID())
              .articleId(UUID.randomUUID())
              .articleTitle("기존 제목 " + i)
              .userId(userId)
              .build();
          ReflectionTestUtils.setField(log, "id", UUID.randomUUID());
          return log;
        }).collect(Collectors.toList());

    userActivity.updateComments(new ArrayList<>(existingComments));

    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));
    given(commentMLogMapper.toEntity(comment, "새로운 제목", user)).willReturn(commentMLog);

    // when
    commentMLogService.addRecentComment(user, comment, "새로운 제목");

    // then
    then(userActivityMongoRepository).should().save(argThat(saved -> {
      List<CommentMLog> comments = saved.getComments();
      boolean correctSize = comments.size() == 10;
      boolean firstIsNew = comments.get(0).getId().equals(commentMLog.getId());
      return correctSize && firstIsNew;
    }));

  }

  @Test
  @DisplayName("최근 댓글 로그 리스트 조회 - 좋아요 수 포함")
  void getCommentRecentSingleLog_success() {

    // given
    List<CommentMLog> commentMLogs = List.of(commentMLog);

    given(commentRepository.findAllById(Set.of(commentMLog.getId()))).willReturn(List.of(comment));
    given(userRepository.findAllById(Set.of(commentMLog.getUserId()))).willReturn(List.of(user));
    given(commentMLogMapper.toResponse(any(), anyInt(), anyString(), anyString()))
        .willReturn(response); // 인자 정확 매치가 안 되면 null 반환되므로 any 사용

    // when
    List<CommentRecentLogResponse> result = commentMLogService.getCommentRecentLogs(commentMLogs);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0))
        .usingRecursiveComparison()
        .isEqualTo(response);

    verify(commentRepository).findAllById(Set.of(commentMLog.getId()));
    verify(userRepository).findAllById(Set.of(user.getId()));
    verify(commentMLogMapper).toResponse(commentMLog, 1, "tester", "테스트 댓글");
  }

}