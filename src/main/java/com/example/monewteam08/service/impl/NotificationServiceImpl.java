package com.example.monewteam08.service.impl;

import static com.example.monewteam08.entity.ResourceType.ARTICLE_INTEREST;
import static com.example.monewteam08.entity.ResourceType.COMMENT;

import com.example.monewteam08.dto.response.nodtification.NotificationDto;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.mapper.NotificationMapper;
import com.example.monewteam08.repository.NotificationRepository;
import com.example.monewteam08.service.Interface.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  public void createArticleNotification(UUID userId, UUID interestId, String interest, int count) {
    String content = String.format("[%s]와 관련된 기사가 %d건 등록되었습니다.", interestId, count);
    Notification notification = new Notification(userId, content, ARTICLE_INTEREST, interestId);
    notificationRepository.save(notification);
  }

  @Override
  public void createCommentLikeNotification(UUID userId, UUID commentId,
      String likerNickname) {
    String content = String.format("[%s]님이 나의 댓글을 좋아합니다.", likerNickname);
    Notification notification = new Notification(userId, content, COMMENT, commentId);
    notificationRepository.save(notification);
  }

  @Transactional
  @Override
  public void confirmNotification(UUID id, UUID userId) {
    Notification notification = notificationRepository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않거나 권한이 없습니다." ));
    notification.confirm();
  }

  @Transactional
  @Override
  public void confirmAllNotifications(UUID userId) {
    List<Notification> notifications = notificationRepository.findAllByUserIdAndIsConfirmedFalse(
        userId);
    notifications.forEach(Notification::confirm);
  }

  @Override
  public List<NotificationDto> getUnreadNotifications(UUID userId, LocalDateTime cursor,
      int limit) {
    List<Notification> result = notificationRepository.findUnreadByUserIdBefore(
        userId, cursor, PageRequest.of(0, limit + 1));
    return result.stream()
        .limit(limit)
        .map(notificationMapper::toDto)
        .toList();
  }

  //배치 삭제
  @Transactional
  @Override
  public void deleteNotification() {
    LocalDateTime threshold = LocalDateTime.now().minusDays(7);
    notificationRepository.deleteByIsConfirmedTrueAndUpdatedAtBefore(threshold);
  }
}
