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
@Table(name = "comment_recent_log", uniqueConstraints = @UniqueConstraint(columnNames = {
    "activity_log_id", "comment_id"}))
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentRecentLog {

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
  @JoinColumn(name = "comment_id", nullable = false)
  private Comment comment;

  @Column(name = "article_id", nullable = false)
  private UUID articleId;

  @Column(name = "article_title")
  private String articleTitle;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "user_nickname")
  private String userNickname;

  @Column(name = "content")
  private String commentContent;

  @Column(name = "comment_created_at")
  private LocalDateTime commentCreatedAt;

  @Builder
  private CommentRecentLog(Comment comment, UUID articleId, String articleTitle, User user,
      String userNickname, String commentContent, LocalDateTime commentCreatedAt,
      UserActivityLog activityLog) {
    this.activityLog = activityLog;
    this.comment = comment;
    this.articleId = articleId;
    this.articleTitle = articleTitle;
    this.user = user;
    this.userNickname = userNickname;
    this.commentContent = commentContent;
    this.commentCreatedAt = commentCreatedAt;
  }

}
