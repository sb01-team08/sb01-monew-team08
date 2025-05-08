package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.entity.CommentLikeMLog;
import com.example.monewteam08.entity.CommentMLog;
import com.example.monewteam08.entity.NewsViewMLog;
import com.example.monewteam08.entity.SubscriptionMLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.mapper.UserActivityMapper;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentLikeMLogService;
import com.example.monewteam08.service.Interface.CommentMLogService;
import com.example.monewteam08.service.Interface.NewsViewMLogService;
import com.example.monewteam08.service.Interface.SubscriptionMLogService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
class UserActivityMServiceImplTest {

  @InjectMocks
  private UserActivityMServiceImpl userActivityService;

  @Mock
  private UserActivityMongoRepository userActivityMongoRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserActivityMapper userActivityMapper;
  @Mock
  private CommentMLogService commentMLogService;
  @Mock
  private CommentLikeMLogService commentLikeMLogService;
  @Mock
  private NewsViewMLogService newsViewMLogService;
  @Mock
  private SubscriptionMLogService subscriptionMLogService;


  @Test
  @DisplayName("UserActivity 생성 성공")
  void createUserActivity_success() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("test@test.com", "tester", "!test1234");
    ReflectionTestUtils.setField(user, "id", userId);

    UserActivity userActivity = UserActivity.builder()
        .id(userId)
        .email("test@test.com")
        .nickname("tester")
        .build();

    given(userActivityMapper.toEntity(user)).willReturn(userActivity);

    // when
    userActivityService.createUserActivity(user);

    // then
    verify(userActivityMapper).toEntity(user);
    verify(userActivityMongoRepository).save(userActivity);
  }
  
  @Test
  @DisplayName("UserActivity 조회 성공")
  void getUserActivityLog_success() {
    // given
    UUID userId = UUID.randomUUID();
    String nickname = "tester";
    String email = "tester@example.com";

    // mock user
    User user = mock(User.class);
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(user.getNickname()).willReturn(nickname);
    given(user.getEmail()).willReturn(email);

    // logs
    CommentMLog commentMLog = CommentMLog.builder().id(UUID.randomUUID()).userId(userId).build();
    CommentLikeMLog likeMLog = CommentLikeMLog.builder().id(UUID.randomUUID())
        .commentId(UUID.randomUUID()).commentUserId(userId).build();
    NewsViewMLog viewMLog = NewsViewMLog.builder().build();
    SubscriptionMLog subscriptionMLog = SubscriptionMLog.builder().id(UUID.randomUUID()).build();

    List<CommentMLog> comments = List.of(commentMLog);
    List<CommentLikeMLog> likes = List.of(likeMLog);
    List<NewsViewMLog> views = List.of(viewMLog);
    List<SubscriptionMLog> subs = List.of(subscriptionMLog);

    // user activity
    UserActivity userActivity = UserActivity.builder()
        .id(userId)
        .comments(comments)
        .commentLikes(likes)
        .articleViews(views)
        .subscriptions(subs)
        .build();

    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));

    // responses
    List<CommentRecentLogResponse> commentResponses = List.of(
        new CommentRecentLogResponse(UUID.randomUUID(), UUID.randomUUID(), "title", userId,
            nickname, "content", 3, LocalDateTime.now())
    );
    List<CommentLikeLogResponse> likeResponses = List.of(
        new CommentLikeLogResponse(UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(),
            UUID.randomUUID(), "title", userId, nickname, "comment", 2, LocalDateTime.now())
    );
    List<NewsViewLogResponse> viewResponses = List.of(
        new NewsViewLogResponse(UUID.randomUUID(), userId, LocalDateTime.now(), UUID.randomUUID(),
            "source", "http://url", "article", LocalDateTime.now(), "summary", 1, 5)
    );
    List<UserActivitySubscriptionResponse> subscriptionResponses = List.of(
        new UserActivitySubscriptionResponse(UUID.randomUUID(), UUID.randomUUID(), "interest",
            List.of("key1", "key2"), 10, LocalDateTime.now())
    );

    // service mocks
    given(commentMLogService.getCommentRecentLogs(comments)).willReturn(commentResponses);
    given(commentLikeMLogService.getCommentLikeLogs(likes)).willReturn(likeResponses);
    given(newsViewMLogService.getNewsViewLogs(views)).willReturn(viewResponses);
    given(subscriptionMLogService.getSubscriptionLogs(subs)).willReturn(subscriptionResponses);

    // final response
    UserActivityLogResponse expected = new UserActivityLogResponse(
        userId,
        email,
        nickname,
        userActivity.getCreatedAt(),
        subscriptionResponses,
        commentResponses,
        likeResponses,
        viewResponses
    );

    given(userActivityMapper.toResponse(any(), anyString(), anyList(), anyList(), anyList(),
        anyList()))
        .willReturn(expected);

    // when
    UserActivityLogResponse result = userActivityService.getUserActivityLog(userId);

    // then
    assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    verify(userRepository).findById(userId);
    verify(userActivityMongoRepository).findById(userId);
    verify(commentMLogService).getCommentRecentLogs(comments);
    verify(commentLikeMLogService).getCommentLikeLogs(likes);
    verify(newsViewMLogService).getNewsViewLogs(views);
    verify(subscriptionMLogService).getSubscriptionLogs(subs);
    verify(userActivityMapper).toResponse(userActivity, nickname, subscriptionResponses,
        commentResponses, likeResponses, viewResponses);
  }

}