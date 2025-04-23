package com.example.monewteam08.dto.response.useractivitylog;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CommentLikeLogResponse(UUID id, LocalDateTime createdAt, UUID commentId,
                                     UUID articleId, String articleTitle, UUID commentUserId,
                                     String commentUserNickname, String commentContent,
                                     int commentLikeCount, LocalDateTime commentCreatedAt) {

}
