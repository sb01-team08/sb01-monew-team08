package com.example.monewteam08.dto.response.useractivitylog;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record NewsViewLogResponse(UUID id, UUID viewedBy, LocalDateTime createdAt, UUID articleId,
                                  String source, String sourceUrl, String articleTitle,
                                  LocalDateTime articlePublishedDate, String articleSummary,
                                  int articleCommentCount, int articleViewCount) {

}
