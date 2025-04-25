package com.example.monewteam08.service.impl;

import static com.example.monewteam08.entity.ResourceType.ARTICLE_INTEREST;
import static com.example.monewteam08.entity.ResourceType.COMMENT;

import com.example.monewteam08.dto.response.notification.CursorPageResponseNotificationDto;
import com.example.monewteam08.dto.response.notification.NotificationDto;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.mapper.NotificationMapper;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.NotificationRepository;
import com.example.monewteam08.service.Interface.NotificationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final CommentRepository commentRepository;

  @Override
  public void createArticleNotification(UUID userId, UUID interestId, String interest, int count) {
    String content = String.format("[%s]와 관련된 기사가 %d건 등록되었습니다.", interest, count);
    Notification notification = new Notification(userId, content, ARTICLE_INTEREST, interestId);
    notificationRepository.save(notification);
  }

  @Override
  public void createCommentLikeNotification(UUID userId, UUID commentId,
      String likerNickname) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(CommentNotFoundException::new);
    UUID ownerId = comment.getUserId();
    String content = String.format("[%s]님이 나의 댓글을 좋아합니다.", likerNickname);
    Notification notification = new Notification(ownerId, content, COMMENT, commentId);
    notificationRepository.save(notification);
  }

  @Transactional
  @Override
  public void confirmNotification(String id, String userId) {
    UUID idUuid = UUID.fromString(id);
    UUID userUuid = UUID.fromString(userId);
    Notification notification = notificationRepository.findByIdAndUserId(idUuid, userUuid)
        .orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않거나 권한이 없습니다."));
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    UUID userIdUuid = UUID.fromString(userId);
    LocalDateTime cursorTime =
        (cursor != null && !cursor.isBlank()) ? LocalDateTime.parse(cursor, formatter) : null;
    LocalDateTime afterTime =
        (cursor != null && !cursor.isBlank()) ? LocalDateTime.parse(cursor, formatter) : null;

    List<Notification> result = notificationRepository.findUnreadByCursor(
        userIdUuid, cursorTime, afterTime, limit + 1);

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

    int totalElements = notificationRepository.countByUserIdAndIsConfirmedFalse(userIdUuid);

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
