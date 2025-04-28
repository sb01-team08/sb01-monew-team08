package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentRecentLog;
import com.example.monewteam08.entity.UserActivityLog;
import org.springframework.stereotype.Component;

@Component
public class CommentRecentLogMapper {

  public CommentRecentLogResponse toResponse(CommentRecentLog commentRecentLog) {
    return CommentRecentLogResponse.builder()
        .id(commentRecentLog.getId())
        .articleId(commentRecentLog.getArticleId())
        .articleTitle(commentRecentLog.getArticleTitle())
        .userId(commentRecentLog.getUser().getId())
        .userNickname(commentRecentLog.getUser().getNickname())
        .content(commentRecentLog.getComment().getContent())
        .likeCount(commentRecentLog.getComment().getLikeCount())
        .createdAt(commentRecentLog.getCreatedAt())
        .build();
  }

  public CommentRecentLog toEntity(UserActivityLog userActivityLog, Comment comment, String title) {
    return CommentRecentLog.builder()
        .activityLog(userActivityLog)
        .comment(comment)
        .articleId(comment.getArticleId())
        .articleTitle(title)
        .user(userActivityLog.getUser())
        .userNickname(userActivityLog.getUser().getNickname())
        .commentContent(comment.getContent())
        .commentCreatedAt(comment.getCreatedAt())
        .build();
  }

}
