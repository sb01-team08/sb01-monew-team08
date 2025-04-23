package com.example.monewteam08.service.impl;

import static com.example.monewteam08.entity.ResourceType.ARTICLE_INTEREST;
import static com.example.monewteam08.entity.ResourceType.COMMENT;

import com.example.monewteam08.dto.response.nodtification.CursorPageResponseNotificationDto;
import com.example.monewteam08.dto.response.nodtification.NotificationDto;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.mapper.NotificationMapper;
import com.example.monewteam08.repository.NotificationRepository;
import com.example.monewteam08.service.Interface.NotificationService;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
  public void confirmNotification(String id, String userId) {
    UUID idUuid = UUID.fromString(id);
    UUID userUuid = UUID.fromString(userId);
    Notification notification = notificationRepository.findByIdAndUserId(idUuid, userUuid)
        .orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않거나 권한이 없습니다." ));
    notification.confirm();
  }

  @Transactional
  @Override
  public void confirmAllNotifications(String userId) {
    UUID userUuid = UUID.fromString(userId);
    List<Notification> notifications = notificationRepository.findAllByUserIdAndIsConfirmedFalse(
        userUuid);
    notifications.forEach(Notification::confirm);
  }

  @Override
  public CursorPageResponseNotificationDto getUnreadNotifications(String userId,
      String cursor, String after,
      int limit) {
    UUID userIdUuid = UUID.fromString(userId);
    LocalDateTime cursorFormat = LocalDateTime.parse(cursor);
    LocalDateTime afterFormat = LocalDateTime.parse(after);
    List<Notification> result = notificationRepository.findUnreadByUserIdBefore(
        userIdUuid, cursorFormat, afterFormat, PageRequest.of(0, limit + 1));

    boolean hasNext = result.size() > limit;
    if (hasNext) {
      result = result.subList(0, limit);
    }

    List<NotificationDto> content = result.stream().map(notificationMapper::toDto).toList();

    String nextCursor = null;
    String nextAfter = null;
    if (hasNext && !result.isEmpty()) {
      Notification last = result.get(result.size() - 1);
      nextCursor = last.getCreatedAt().toString();
      nextAfter = last.getId().toString();
    }

    int totalElements = notificationRepository.countByUserId(userIdUuid);

    CursorPageResponseNotificationDto reponse = CursorPageResponseNotificationDto.builder()
        .content(new ArrayList<>(content))
        .nextCursor(nextCursor)
        .nextAfter(nextAfter)
        .size(limit)
        .totalElements(totalElements)
        .hasNext(hasNext)
        .build();
    return reponse;
  }

  //배치 삭제
  @Transactional
  @Override
  public void deleteNotification() {
    LocalDateTime threshold = LocalDateTime.now().minusDays(7);
    notificationRepository.deleteByIsConfirmedTrueAndUpdatedAtBefore(threshold);
  }
}
