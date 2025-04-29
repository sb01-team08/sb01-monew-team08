package com.example.monewteam08.dto.response.article;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record ArticleDto(
    @NotNull UUID id,
    @NotNull String source,
    @NotNull String sourceUrl,
    @NotNull String title,
    @NotNull LocalDateTime publishDate,
    @NotNull String summary,
    long commentCount,
    long viewCount,
    boolean viewedByMe
) {

}
