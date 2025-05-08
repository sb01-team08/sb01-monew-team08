package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.CommentLikeMLog;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeMLogMapper {

  public CommentLikeMLog toEntity(CommentLike commentLike, Comment comment,
      Article article, UUID userId) {
    return CommentLikeMLog.builder()
        .id(commentLike.getId())
        .articleId(article.getId())
        .articleTitle(article.getTitle())
        .commentId(comment.getId())
        .commentUserId(userId)
        .commentCreatedAt(comment.getCreatedAt())
        .build();
  }

  public CommentLikeLogResponse toResponse(CommentLikeMLog commentLikeLog, int commentLikeCount,
      String commentUserNickname, String commentContent) {
    return CommentLikeLogResponse.builder()
        .id(commentLikeLog.getId())
        .createdAt(commentLikeLog.getCreatedAt())
        .commentId(commentLikeLog.getCommentId())
        .articleId(commentLikeLog.getArticleId())
        .articleTitle(commentLikeLog.getArticleTitle())
        .commentUserId(commentLikeLog.getCommentUserId())
        .commentUserNickname(commentUserNickname)
        .commentContent(commentContent)
        .commentCreatedAt(commentLikeLog.getCommentCreatedAt())
        .commentLikeCount(commentLikeCount)
        .build();
  }


}
