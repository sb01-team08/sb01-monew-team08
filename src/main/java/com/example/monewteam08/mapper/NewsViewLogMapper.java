package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.NewsViewLog;
import com.example.monewteam08.entity.UserActivityLog;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class NewsViewLogMapper {

  public NewsViewLog toEntity(UserActivityLog userActivityLog, Article article, UUID userId) {
    return NewsViewLog.builder()
        .activityLog(userActivityLog)
        .viewedBy(userId)
        .articleId(article.getId())
        .articleSummary(article.getSummary())
        .articlePublishedDate(article.getPublishDate())
        .articleTitle(article.getTitle())
        .source(article.getSource())
        .sourceUrl(article.getSourceUrl())
        .build();
  }

  public NewsViewLogResponse toResponse(NewsViewLog newsViewLog, int articleCommentCount,
      int articleViewCount) {
    return NewsViewLogResponse.builder()
        .id(newsViewLog.getId())
        .viewedBy(newsViewLog.getViewedBy())
        .createdAt(newsViewLog.getCreatedAt())
        .articleId(newsViewLog.getArticleId())
        .source(newsViewLog.getSource())
        .sourceUrl(newsViewLog.getSourceUrl())
        .articleTitle(newsViewLog.getArticleTitle())
        .articlePublishedDate(newsViewLog.getArticlePublishedDate())
        .articleSummary(newsViewLog.getArticleSummary())
        .articleCommentCount(articleCommentCount)
        .articleViewCount(articleViewCount)
        .build();
  }

}
