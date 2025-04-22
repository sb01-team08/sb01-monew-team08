package com.example.monewteam08.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "comment_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(name = "comment_id", nullable = false)
    private UUID commentId;

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    public CommentLike(UUID userId, UUID commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
