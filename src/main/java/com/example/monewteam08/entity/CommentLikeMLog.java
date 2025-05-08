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
public class CommentLikeMLog {

  private UUID id;

  @CreatedDate
  private LocalDateTime createdAt;

  private UUID articleId;
  private String articleTitle;

  private UUID commentId;
  private UUID commentUserId;

  private LocalDateTime commentCreatedAt;

  @Builder
  private CommentLikeMLog(UUID id, UUID articleId, String articleTitle, UUID commentId,
      UUID commentUserId, LocalDateTime commentCreatedAt) {
    this.id = id;
    this.articleId = articleId;
    this.articleTitle = articleTitle;
    this.commentId = commentId;
    this.commentUserId = commentUserId;
    this.commentCreatedAt = commentCreatedAt;
    this.createdAt = LocalDateTime.now();
  }
}
