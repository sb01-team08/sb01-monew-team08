package com.example.monewteam08.dto.response.useractivitylog;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CommentRecentLogResponse(UUID id, UUID articleId, String articleTitle, UUID userId,
                                       String userNickname, String content, int likeCount,
                                       LocalDateTime createdAt) {

}
