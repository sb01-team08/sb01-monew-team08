package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.mapper.UserActivityLogMapper;
import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.service.Interface.CommentLikeLogService;
import com.example.monewteam08.service.Interface.CommentRecentLogService;
import com.example.monewteam08.service.Interface.NewsViewLogService;
import com.example.monewteam08.service.Interface.SubscriptionService;
import com.example.monewteam08.service.Interface.UserActivityLogService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivityLogServiceImpl implements UserActivityLogService {

  private final UserActivityLogRepository userActivityLogRepository;
  private final CommentRecentLogService commentRecentLogService;
  private final CommentLikeLogService commentLikeLogService;
  private final NewsViewLogService newsViewLogService;
  private final UserActivityLogMapper userActivityLogMapper;
  private final SubscriptionService subscriptionService;

  @Transactional(readOnly = true)
  @Override
  public UserActivityLogResponse getUserActivityLog(UUID userId) {
    // todo: exception
    UserActivityLog userActivityLog = userActivityLogRepository.findByUserId(userId).orElseThrow();

    List<UserActivitySubscriptionResponse> subscriptionResponses = subscriptionService.getSubscribeForUserActivity(
        userId);
    List<CommentRecentLogResponse> commentRecentLogResponses = commentRecentLogService.getCommentRecentLogs(
        userActivityLog);
    List<CommentLikeLogResponse> commentLikeLogResponses = commentLikeLogService.getCommentLikeLogs(
        userActivityLog);
    List<NewsViewLogResponse> newsViewLogResponses = newsViewLogService.getNewsViewLogs(
        userActivityLog);

    return userActivityLogMapper.toResponse(userActivityLog.getUser(), subscriptionResponses,
        commentRecentLogResponses, commentLikeLogResponses, newsViewLogResponses);
  }
}
