package com.example.monewteam08.dto.response.article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ArticleRestoreResultDto(
    LocalDateTime restoreDate,
    List<UUID> restoredArticleIds,
    long restoredArticleCount
) {

}
