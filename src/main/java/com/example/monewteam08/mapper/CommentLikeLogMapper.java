package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLikeLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentLikeLogMapper {

  public CommentLikeLogResponse toResponse(CommentLikeLog commentLikeLog) {
    return CommentLikeLogResponse.builder()
        .id(commentLikeLog.getId())
        .createdAt(commentLikeLog.getCreatedAt())
        .commentId(commentLikeLog.getComment().getId())
        .articleId(commentLikeLog.getArticleId())
        .articleTitle(commentLikeLog.getArticleTitle())
        .commentUserId(commentLikeLog.getCommentUser().getId())
        .commentUserNickname(commentLikeLog.getCommentUser().getNickname())
        .commentContent(commentLikeLog.getComment().getContent())
        .commentLikeCount(commentLikeLog.getComment().getLikeCount())
        .commentCreatedAt(commentLikeLog.getCommentCreatedAt())
        .build();
  }

  public CommentLikeLog toEntity(UserActivityLog userActivityLog, Comment comment,
      Article article, User commentUser) {
    return CommentLikeLog.builder()
        .activityLog(userActivityLog)
        .comment(comment)
        .articleId(article.getId())
        .articleTitle(article.getTitle())
        .commentUser(commentUser)
        .commentUserNickname(commentUser.getNickname())
        .commentContent(comment.getContent())
        .commentCreatedAt(comment.getCreatedAt())
        .build();
  }

}
