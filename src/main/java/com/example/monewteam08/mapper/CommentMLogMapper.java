package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentMLog;
import com.example.monewteam08.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMLogMapper {

  public CommentMLog toEntity(Comment comment, String title, User user) {
    return CommentMLog.builder()
        .id(comment.getId())
        .articleId(comment.getArticleId())
        .articleTitle(title)
        .userId(user.getId())
        .build();
  }

  public CommentRecentLogResponse toResponse(CommentMLog commentRecentLog, int likeCount,
      String nickname, String content) {
    return CommentRecentLogResponse.builder()
        .id(commentRecentLog.getId())
        .articleId(commentRecentLog.getArticleId())
        .articleTitle(commentRecentLog.getArticleTitle())
        .userId(commentRecentLog.getUserId())
        .userNickname(nickname)
        .content(content)
        .likeCount(likeCount)
        .createdAt(commentRecentLog.getCreatedAt())
        .build();
  }


}
