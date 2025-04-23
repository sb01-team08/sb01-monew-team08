package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.nodtification.CursorPageResponseNotificationDto;
import java.util.UUID;

public interface NotificationService {

  void createArticleNotification(UUID userId, UUID interestId, String interest, int count);

  void createCommentLikeNotification(UUID userId, UUID interestId, String likerNickname);

  void confirmNotification(String notificationId, String userId);

  void confirmAllNotifications(String userId);

  CursorPageResponseNotificationDto getUnreadNotifications(String userId, String cursor,
      String after,
      int limit);

  void deleteNotification();
}
