package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

  public CommentDto toDto(Comment comment, String userNickname, boolean likedByMe) {
    if (comment == null) {
      return null;
    }
    return CommentDto.builder()
        .id(comment.getId().toString())
        .articleId(comment.getArticleId().toString())
        .userId(comment.getUserId().toString())
        .userNickname(userNickname)
        .content(comment.getContent())
        .likeCount(comment.getLikeCount())
        .likedByMe(likedByMe)
        .createdAt(comment.getCreatedAt())
        .build();
  }
}