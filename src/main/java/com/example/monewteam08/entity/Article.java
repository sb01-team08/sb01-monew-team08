package com.example.monewteam08.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "source")
  private String source;

  @Column(name = "title")
  private String title;

  @Column(name = "summary", columnDefinition = "TEXT")
  private String summary;

  @Column(name = "source_url", unique = true)
  private String sourceUrl;

  @Column(name = "published_at")
  private LocalDateTime publishDate;

  @Column(name = "view_count")
  private long viewCount;

  @Column(name = "is_active")
  private boolean isActive;

  public Article(String source, String title, String summary, String sourceUrl,
      LocalDateTime publishDate) {
    this.source = source;
    this.title = title;
    this.summary = summary;
    this.sourceUrl = sourceUrl;
    this.publishDate = publishDate;
    this.isActive = true;
  }

  public void softDelete() {
    this.isActive = false;
  }

  public void addViewCount() {
    this.viewCount++;
  }
}
