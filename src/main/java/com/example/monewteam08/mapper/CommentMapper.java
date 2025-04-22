package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

  public CommentDto toDto(Comment comment) {
    if (comment == null) {
      return null;
    }
    return CommentDto.builder()
        .id(comment.getId().toString())
        .articleId(comment.getArticleId().toString())
        .userId(comment.getUserId().toString())
        .content(comment.getContent())
        .likeCount(comment.getLikeCount())
        .createdAt(comment.getCreatedAt())
        .likedByMe(false) //추후 수정
        .userNickname(null) //추후 수정
        .build();
  }
}