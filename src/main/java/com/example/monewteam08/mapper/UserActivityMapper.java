package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserActivityMapper {

  public UserActivity toEntity(User user) {
    return UserActivity.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .subscriptions(new ArrayList<>())
        .comments(new ArrayList<>())
        .commentLikes(new ArrayList<>())
        .articleViews(new ArrayList<>())
        .build();
  }

  public UserActivityLogResponse toResponse(UserActivity userActivity, String userNickname,
      List<UserActivitySubscriptionResponse> subscriptions,
      List<CommentRecentLogResponse> commentRecentLogResponses,
      List<CommentLikeLogResponse> commentLikeLogResponses,
      List<NewsViewLogResponse> newsViewLogResponses) {

    return UserActivityLogResponse.builder()
        .id(userActivity.getId())
        .email(userActivity.getEmail())
        .nickname(userNickname)
        .createdAt(userActivity.getCreatedAt())
        .subscriptions(subscriptions)
        .comments(commentRecentLogResponses)
        .commentLikes(commentLikeLogResponses)
        .articleViews(newsViewLogResponses)
        .build();
  }

}