package com.example.monewteam08.service.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.exception.useractivitylog.UserActicityLogNotFoundException;
import com.example.monewteam08.mapper.UserActivityLogMapper;
import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.service.Interface.CommentLikeLogService;
import com.example.monewteam08.service.Interface.CommentRecentLogService;
import com.example.monewteam08.service.Interface.NewsViewLogService;
import com.example.monewteam08.service.Interface.SubscriptionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserActivityLogServiceImplTest {

  @InjectMocks
  private UserActivityLogServiceImpl userActivityLogService;

  @Mock
  private UserActivityLogRepository userActivityLogRepository;
  @Mock
  private SubscriptionService subscriptionService;
  @Mock
  private CommentRecentLogService commentRecentLogService;
  @Mock
  private CommentLikeLogService commentLikeLogService;
  @Mock
  private NewsViewLogService newsViewLogService;
  @Mock
  private UserActivityLogMapper userActivityLogMapper;

  @Test
  @DisplayName("사용자 활동 내역을 전달한다.")
  void getUserActivityLog() {
    // given
    UUID userId = UUID.randomUUID();
    UserActivityLog userActivityLog = mock(UserActivityLog.class);
    User user = mock(User.class);

    given(userActivityLogRepository.findByUserId(userId)).willReturn(Optional.of(userActivityLog));
    given(userActivityLog.getUser()).willReturn(user);

    List<UserActivitySubscriptionResponse> subscriptionResponses = List.of(mock(
        UserActivitySubscriptionResponse.class));
    List<CommentRecentLogResponse> commentRecentLogResponses = List.of(mock(
        CommentRecentLogResponse.class));
    List<CommentLikeLogResponse> commentLikeLogResponses = List.of(
        mock(CommentLikeLogResponse.class));
    List<NewsViewLogResponse> newsViewLogResponses = List.of(mock(NewsViewLogResponse.class));
    UserActivityLogResponse expectedResponse = mock(UserActivityLogResponse.class);

    given(subscriptionService.getSubscribeForUserActivity(userId)).willReturn(
        subscriptionResponses);
    given(commentRecentLogService.getCommentRecentLogs(userActivityLog)).willReturn(
        commentRecentLogResponses);
    given(commentLikeLogService.getCommentLikeLogs(userActivityLog)).willReturn(
        commentLikeLogResponses);
    given(newsViewLogService.getNewsViewLogs(userActivityLog)).willReturn(newsViewLogResponses);
    given(userActivityLogMapper.toResponse(user, subscriptionResponses, commentRecentLogResponses,
        commentLikeLogResponses, newsViewLogResponses))
        .willReturn(expectedResponse);

    // when
    UserActivityLogResponse actualResponse = userActivityLogService.getUserActivityLog(userId);

    // then
    Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  @DisplayName("사용자 활동 내역을 사용자 아이디로 찾지 못해 예외를 반환한다.")
  void failed_getUserActivityLog() {
    // given
    UUID userId = UUID.randomUUID();
    UserActivityLog userActivityLog = mock(UserActivityLog.class);
    User user = mock(User.class);

    given(userActivityLogRepository.findByUserId(userId)).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> userActivityLogService.getUserActivityLog(userId))
        .isInstanceOf(UserActicityLogNotFoundException.class);
  }


}