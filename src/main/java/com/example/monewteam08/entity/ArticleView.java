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
@Table(name = "user_article_views")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ArticleView {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "article_id", nullable = false)
  private UUID articleId;

  @Column(name = "viewed_at")
  private LocalDateTime viewedAt = LocalDateTime.now();

  public ArticleView(UUID userId, UUID articleId) {
    this.userId = userId;
    this.articleId = articleId;
    this.viewedAt = LocalDateTime.now();
  }
}
