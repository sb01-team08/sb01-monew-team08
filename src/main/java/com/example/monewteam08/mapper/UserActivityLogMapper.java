package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserActivityLogMapper {

  public UserActivityLogResponse toResponse(User user,
      List<UserActivitySubscriptionResponse> subscriptions,
      List<CommentRecentLogResponse> commentRecentLogResponses,
      List<CommentLikeLogResponse> commentLikeLogResponses,
      List<NewsViewLogResponse> newsViewLogResponses) {

    return UserActivityLogResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .createdAt(user.getCreatedAt())
        .subscriptions(subscriptions)
        .comments(commentRecentLogResponses)
        .commentLikes(commentLikeLogResponses)
        .articleViews(newsViewLogResponses)
        .build();
  }

}
