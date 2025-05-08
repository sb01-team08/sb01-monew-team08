package com.example.monewteam08.entity;

import jakarta.persistence.EntityListeners;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class NewsViewMLog {

  private UUID id;

  @CreatedDate
  private LocalDateTime createdAt;

  private UUID viewedBy;
  private UUID articleId;
  private String source;
  private String sourceUrl;
  private String articleTitle;
  private LocalDateTime articlePublishedDate;
  private String articleSummary;

  @Builder
  private NewsViewMLog(UUID viewedBy, UUID articleId,
      String source, String sourceUrl, String articleTitle, LocalDateTime articlePublishedDate,
      String articleSummary) {
    this.id = UUID.randomUUID();
    this.viewedBy = viewedBy;
    this.articleId = articleId;
    this.source = source;
    this.sourceUrl = sourceUrl;
    this.articleTitle = articleTitle;
    this.articlePublishedDate = articlePublishedDate;
    this.articleSummary = articleSummary;
    this.createdAt = LocalDateTime.now();
  }
}
