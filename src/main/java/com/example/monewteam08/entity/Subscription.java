package com.example.monewteam08.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_interest_subscriptions")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "interest_id", nullable = false)
  private UUID interestId;

  @CreatedDate
  @Column(name = "subscription_date", nullable = false, updatable = false)
  private LocalDateTime subscriptionDate;

  public Subscription(UUID id, UUID userId, UUID interestId) {
    this.id = id;
    this.userId = userId;
    this.interestId = interestId;
  }

  public Subscription(UUID userId, UUID interestId) {
    this.userId = userId;
    this.interestId = interestId;
  }

}
