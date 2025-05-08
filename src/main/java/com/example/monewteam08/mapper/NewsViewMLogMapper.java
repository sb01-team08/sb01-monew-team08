package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.NewsViewMLog;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class NewsViewMLogMapper {

  public NewsViewMLog toEntity(Article article, UUID userId) {
    return NewsViewMLog.builder()
        .viewedBy(userId)
        .articleId(article.getId())
        .source(article.getSource())
        .sourceUrl(article.getSourceUrl())
        .articleTitle(article.getTitle())
        .articlePublishedDate(article.getPublishDate())
        .articleSummary(article.getSummary())
        .build();
  }

  public NewsViewLogResponse toResponse(NewsViewMLog newsViewLog, int articleCommentCount,
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
