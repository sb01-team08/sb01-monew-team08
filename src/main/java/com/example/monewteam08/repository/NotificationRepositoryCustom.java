package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationRepositoryCustom {

  List<Notification> findUnreadByCursor(UUID userId, LocalDateTime cursor, UUID after, int limit);
}
