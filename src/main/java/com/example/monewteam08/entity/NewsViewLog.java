package com.example.monewteam08.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "news_view_log", uniqueConstraints = @UniqueConstraint(columnNames = {
    "activity_log_id", "article_id"}))
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NewsViewLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_log_id", nullable = false)
  private UserActivityLog activityLog;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false, insertable = true)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id", nullable = false)
  private Article article;

  @Column(name = "viewed_by", nullable = false)
  private UUID viewedBy;

  @Column(name = "source")
  private String source;

  @Column(name = "source_url")
  private String sourceUrl;

  @Column(name = "article_title")
  private String articleTitle;

  @Column(name = "article_published_date")
  private LocalDateTime articlePublishedDate;

  @Column(name = "article_summary")
  private String articleSummary;

  @Builder
  private NewsViewLog(String articleSummary, LocalDateTime articlePublishedDate,
      String articleTitle, UUID viewedBy, String sourceUrl, String source, Article article,
      UserActivityLog activityLog) {
    this.articleSummary = articleSummary;
    this.articlePublishedDate = articlePublishedDate;
    this.articleTitle = articleTitle;
    this.viewedBy = viewedBy;
    this.sourceUrl = sourceUrl;
    this.source = source;
    this.article = article;
    this.activityLog = activityLog;
  }
}
