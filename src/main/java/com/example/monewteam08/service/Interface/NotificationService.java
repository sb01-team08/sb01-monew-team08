package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.nodtification.NotificationDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  void createArticleNotification(UUID userId, UUID interestId, String interest, int count);

  void createCommentLikeNotification(UUID userId, UUID interestId, String likerNickname);

  void confirmNotification(UUID notificationId, UUID userId);

  void confirmAllNotifications(UUID userId);

  List<NotificationDto> getUnreadNotifications(UUID userId, LocalDateTime cursor, int limit);

  void deleteNotification();
}
