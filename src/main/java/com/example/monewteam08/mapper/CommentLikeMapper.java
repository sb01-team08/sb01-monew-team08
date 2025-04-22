package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.comment.CommentLikeDto;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeMapper {

  public CommentLikeDto toDto(CommentLike commentLike, Comment comment, String nickName) {
    return CommentLikeDto.builder()
        .id(commentLike.getId().toString())
        .likedBy(commentLike.getUserId().toString())
        .createdAt(commentLike.getCreatedAt())
        .commentId(comment.getId().toString())
        .articleId(comment.getArticleId().toString())
        .commentUserId(comment.getUserId().toString())
        .commentUserNickname(nickName)
        .commentContent(comment.getContent())
        .commentLikeCount(comment.getLikeCount())
        .commentCreatedAt(comment.getCreatedAt())
        .build();
  }
}
