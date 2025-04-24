package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLikeLog;
import com.example.monewteam08.entity.UserActivityLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentLikeLogMapper {

  public CommentLikeLogResponse toResponse(CommentLikeLog commentLikeLog, int commentLikeCount) {
    return CommentLikeLogResponse.builder()
        .id(commentLikeLog.getId())
        .createdAt(commentLikeLog.getCreatedAt())
        .commentId(commentLikeLog.getCommentId())
        .articleId(commentLikeLog.getArticleId())
        .articleTitle(commentLikeLog.getArticleTitle())
        .commentUserId(commentLikeLog.getCommentUserId())
        .commentUserNickname(commentLikeLog.getCommentUserNickname())
        .commentContent(commentLikeLog.getCommentContent())
        .commentLikeCount(commentLikeCount)
        .commentCreatedAt(commentLikeLog.getCommentCreatedAt())
        .build();
  }

  public CommentLikeLog toEntity(UserActivityLog userActivityLog, Comment comment,
      Article article) {
    return CommentLikeLog.builder()
        .activityLog(userActivityLog)
        .commentId(comment.getId())
        .articleId(article.getId())
        .articleTitle(article.getTitle())
        .commentUserId(comment.getUserId())
        .commentUserNickname(userActivityLog.getUser().getNickname())
        .commentContent(comment.getContent())
        .commentCreatedAt(comment.getCreatedAt())
        .build();
  }

}
