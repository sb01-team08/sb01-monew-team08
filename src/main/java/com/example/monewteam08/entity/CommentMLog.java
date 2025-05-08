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
public class CommentMLog {

  private UUID id;

  @CreatedDate
  private LocalDateTime createdAt;

  private UUID articleId;
  private String articleTitle;

  private UUID userId;

  @Builder
  private CommentMLog(UUID id, UUID articleId, String articleTitle, UUID userId) {
    this.id = id;
    this.articleId = articleId;
    this.articleTitle = articleTitle;
    this.userId = userId;
    this.createdAt = LocalDateTime.now();
  }
}
