package com.example.monewteam08.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "notifications" )
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;
  @Column(columnDefinition = "TEXT" )
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "resource_type", length = 30, nullable = false)
  private ResourceType resource_type;
  @Column(name = "resource_id", nullable = false)
  private UUID resourceId;
  @Column(name = "is_confirmed" )
  private Boolean isConfirmed = false;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "update_at" )
  private LocalDateTime updatedAt;

  public void confirm() {
    isConfirmed = true;
    this.updatedAt = LocalDateTime.now();
  }

  public Notification(UUID userId, String content, ResourceType resource_type, UUID resourceId) {
    this.userId = userId;
    this.content = content;
    this.resource_type = resource_type;
    this.resourceId = resourceId;
  }
}
