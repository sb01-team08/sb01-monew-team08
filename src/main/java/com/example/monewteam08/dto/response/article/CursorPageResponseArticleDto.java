package com.example.monewteam08.dto.response.article;

import java.time.LocalDateTime;
import java.util.List;

public record CursorPageResponseArticleDto(
    List<ArticleDto> content,
    String nextCursor,
    LocalDateTime nextAfter,
    int size,
    long totalElements,
    boolean hasNext
) {

}
