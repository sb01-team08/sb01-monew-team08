package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentRecentLog;
import com.example.monewteam08.entity.UserActivityLog;
import org.springframework.stereotype.Component;

@Component
public class CommentRecentLogMapper {

  public CommentRecentLogResponse toResponse(CommentRecentLog commentRecentLog, int likeCount) {
    return CommentRecentLogResponse.builder()
        .id(commentRecentLog.getId())
        .articleId(commentRecentLog.getArticleId())
        .articleTitle(commentRecentLog.getArticleTitle())
        .userId(commentRecentLog.getUserId())
        .userNickname(commentRecentLog.getUserNickname())
        .content(commentRecentLog.getCommentContent())
        .likeCount(likeCount)
        .createdAt(commentRecentLog.getCreatedAt())
        .build();
  }

  public CommentRecentLog toEntity(UserActivityLog userActivityLog, Comment comment, String title) {
    return CommentRecentLog.builder()
        .activityLog(userActivityLog)
        .commentId(comment.getId())
        .articleId(comment.getArticleId())
        .articleTitle(title)
        .userId(comment.getUserId())
        .userNickname(userActivityLog.getUser().getNickname())
        .commentContent(comment.getContent())
        .commentCreatedAt(comment.getCreatedAt())
        .build();
  }

}
