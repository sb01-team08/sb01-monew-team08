package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.comment.CommentLikeDto;
import java.util.UUID;

public interface CommentLikeService {

    CommentLikeDto like(UUID userId, UUID commentId);

    void unlike(UUID userId, UUID commentId);
}
