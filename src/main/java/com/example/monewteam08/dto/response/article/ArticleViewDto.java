package com.example.monewteam08.dto.response.article;

import java.time.LocalDateTime;
import java.util.UUID;

public record ArticleViewDto(
    UUID id,
    UUID viewedBy,
    LocalDateTime createdAt,
    UUID articleId,
    String source,
    String sourceUrl,
    String articleTitle,
    LocalDateTime articlePublishedDate,
    String articleSummary,
    long articleCommentCount,
    long articleViewCount
) {

}
