package com.example.monewteam08.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB로 연결하기 위해 작성된 내용, 해당 부분이 제대로 적용되면 아래 엔티티는 삭제해도 됨 - UserActivityLog - CommentLikeLog -
 * CommentRecentLog - NewsViewLog
 */

@Document(collection = "user_activities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserActivity {

  @Id
  private UUID id;

  @CreatedDate
  private LocalDateTime createdAt;

  private String email;
  private String nickname;

  private List<SubscriptionMLog> subscriptions;
  private List<CommentMLog> comments;
  private List<CommentLikeMLog> commentLikes;
  private List<NewsViewMLog> articleViews;

  @Builder
  private UserActivity(UUID id, String email, String nickname, List<SubscriptionMLog> subscriptions,
      List<CommentMLog> comments, List<CommentLikeMLog> commentLikes,
      List<NewsViewMLog> articleViews) {
    this.id = id;
    this.email = email;
    this.nickname = nickname;
    this.subscriptions = subscriptions;
    this.comments = comments;
    this.commentLikes = commentLikes;
    this.articleViews = articleViews;
    this.createdAt = LocalDateTime.now();
  }

  public void updateSubscriptions(
      List<SubscriptionMLog> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public void updateComments(List<CommentMLog> comments) {
    this.comments = comments;
  }

  public void updateCommentLikes(List<CommentLikeMLog> commentLikes) {
    this.commentLikes = commentLikes;
  }

  public void updateArticleViews(List<NewsViewMLog> articleViews) {
    this.articleViews = articleViews;
  }
}
