package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  Optional<Notification> findByIdAndUserId(UUID id, UUID userId);

  List<Notification> findAllByUserIdAndIsConfirmedFalse(UUID userId);

  void deleteByIsConfirmedTrueAndUpdatedAtBefore(LocalDateTime threshold);

  @Query("SELECT n FROM Notification n " +
      "WHERE n.userId = :userId " +
      "and n.isConfirmed=false " +
      "and (:cursor IS NULL OR  n.createdAt <: cursor) " +
      "order by n.createdAt DESC "
  )
  List<Notification> findUnreadByUserIdBefore(
      @Param("userId" ) UUID userIdBefore,
      @Param("cursor" ) LocalDateTime cursor,
      Pageable pageable
  );
}
