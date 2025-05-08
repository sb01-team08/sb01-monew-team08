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
public class SubscriptionMLog {

  private UUID id;

  @CreatedDate
  private LocalDateTime createdAt;

  private UUID interestId;

  @Builder
  private SubscriptionMLog(UUID id, UUID interestId) {
    this.id = id;
    this.interestId = interestId;
    this.createdAt = LocalDateTime.now();
  }
}
