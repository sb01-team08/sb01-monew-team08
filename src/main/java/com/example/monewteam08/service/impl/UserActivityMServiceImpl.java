package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.UserActivityMapper;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentLikeMLogService;
import com.example.monewteam08.service.Interface.CommentMLogService;
import com.example.monewteam08.service.Interface.NewsViewMLogService;
import com.example.monewteam08.service.Interface.SubscriptionMLogService;
import com.example.monewteam08.service.Interface.UserActivityMService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivityMServiceImpl implements UserActivityMService {

  private final UserActivityMapper userActivityMapper;
  private final UserActivityMongoRepository userActivityMongoRepository;
  private final CommentMLogService commentMLogService;
  private final CommentLikeMLogService commentLikeMLogService;
  private final NewsViewMLogService newsViewMLogService;
  private final SubscriptionMLogService subscriptionMLogService;
  private final UserRepository userRepository;

  @Override
  public void createUserActivity(User user) {
    UserActivity userActivity = userActivityMapper.toEntity(user);
    userActivityMongoRepository.save(userActivity);
  }

  @Override
  public UserActivityLogResponse getUserActivityLog(UUID userId) {
    // todo: exception
    UserActivity userActivity = userActivityMongoRepository.findById(userId).orElseThrow();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    List<CommentRecentLogResponse> commentRecentLogResponseList = commentMLogService.getCommentRecentLogs(
        userActivity.getComments());
    List<CommentLikeLogResponse> commentLikeLogResponseList = commentLikeMLogService.getCommentLikeLogs(
        userActivity.getCommentLikes());
    List<NewsViewLogResponse> newsViewLogResponseList = newsViewMLogService.getNewsViewLogs(
        userActivity.getArticleViews());
    List<UserActivitySubscriptionResponse> subscriptionResponseList = subscriptionMLogService.getSubscriptionLogs(
        userActivity.getSubscriptions());

    return userActivityMapper.toResponse(userActivity, user.getNickname(), subscriptionResponseList,
        commentRecentLogResponseList, commentLikeLogResponseList, newsViewLogResponseList);
  }
}
