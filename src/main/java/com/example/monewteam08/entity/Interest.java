package com.example.monewteam08.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "interests")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Interest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @ElementCollection
  @CollectionTable(name = "interest_keywords", joinColumns = @JoinColumn(name = "interest_id"))
  @Column(name = "keywords")
  private List<String> keywords;

  @Column(name = "subscriber_count")
  private int subscriberCount;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public Interest(UUID id, String name, List<String> keywords, int subscriberCount) {
    this.id = id;
    this.name = name;
    this.keywords = keywords;
    this.subscriberCount = subscriberCount;
  }

  public Interest(String name, List<String> keywords) {
    this.name = name;
    this.keywords = keywords;
    this.subscriberCount = 0;
  }

  public void updateKeywords(List<String> newKeywords) {
    this.keywords = newKeywords;
  }

  public void increaseSubscriberCount() {
    this.subscriberCount++;
  }

  public void decreaseSubscriberCount() {
    this.subscriberCount = Math.max(0, this.subscriberCount - 1);
  }
}
