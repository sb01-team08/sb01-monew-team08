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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * - User 엔티티
 */

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "email")
  private String email;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "password")
  private String password;

  @Column(name = "is_active")
  private boolean isActive;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false, insertable = true)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = true, updatable = true, insertable = true)
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at", nullable = true, updatable = true, insertable = true)
  private LocalDateTime deletedAt;

  public User(String email, String nickname, String password) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.isActive = true;
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  public void updateDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public void updateIsActive(boolean isActive) {
    this.isActive = isActive;
  }
}
