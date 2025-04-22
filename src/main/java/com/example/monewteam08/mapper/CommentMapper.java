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
                .userNickname(null) //추후 수정
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .likedByMe(false) //추후 수정
                .createdAt(comment.getCreatedAt())
                .build();
    }
}