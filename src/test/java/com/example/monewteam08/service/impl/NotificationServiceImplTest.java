package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.notification.CursorPageResponseNotificationDto;
import com.example.monewteam08.dto.response.notification.NotificationDto;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.entity.ResourceType;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.mapper.NotificationMapper;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.NotificationRepository;
import com.example.monewteam08.repository.NotificationRepositoryCustom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private NotificationRepositoryCustom notificationQueryRepository;

  @Mock
  private NotificationMapper notificationMapper;

  @InjectMocks
  private NotificationServiceImpl notificationService;

  private UUID userId;
  private UUID resourceId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    resourceId = UUID.randomUUID();
  }

  @Test
  void 알림_생성_성공_기사() {
    notificationService.createArticleNotification(userId, resourceId, "운동", 3);
    verify(notificationRepository).save(any(Notification.class));
  }

  @Test
  void 알림_생성_성공_댓글_좋아요() {
    UUID commentId = resourceId;
    UUID ownerId = UUID.randomUUID();
    Comment comment = new Comment(UUID.randomUUID(), ownerId, "댓글");
    given(commentRepository.findById(eq(commentId))).willReturn(Optional.of(comment));

    notificationService.createCommentLikeNotification(userId, commentId, "홍길동");

    verify(notificationRepository).save(any(Notification.class));
  }

  @Test
  void 알림_단건_확인() {
    Notification mock = mock(Notification.class);
    given(notificationRepository.findByIdAndUserId(resourceId, userId)).willReturn(
        Optional.of(mock));

    notificationService.confirmNotification(resourceId.toString(), userId.toString());

    verify(mock).confirm();
  }

  @Test
  void 알림_쩐체_확인() {
    Notification mock1 = mock(Notification.class);
    Notification mock2 = mock(Notification.class);

    List<Notification> mockList = List.of(mock1, mock2);

    given(notificationRepository.findAllByUserIdAndIsConfirmedFalse(userId)).willReturn(mockList);

    notificationService.confirmAllNotifications(userId.toString());

    verify(mock1).confirm();
    verify(mock2).confirm();
  }

  @Test
  void 읽지_않은_알림_조회() {
    Notification notification1 = new Notification(userId, "알림1", ResourceType.COMMENT,
        UUID.randomUUID());
    Notification notification2 = new Notification(userId, "알림2", ResourceType.COMMENT,
        UUID.randomUUID());

    List<Notification> mockList = List.of(notification1, notification2);
    NotificationDto dto1 = NotificationDto.builder()
        .id(UUID.randomUUID().toString())
        .createdAt(notification1.getCreatedAt())
        .updatedAt(notification1.getUpdatedAt())
        .confirmed(notification1.getIsConfirmed())
        .userId(notification1.getUserId().toString())
        .content(notification1.getContent())
        .resourceType(notification1.getResource_type().name())
        .resourceId(notification1.getResourceId().toString())
        .build();
    NotificationDto dto2 = NotificationDto.builder()
        .id(UUID.randomUUID().toString())
        .createdAt(notification2.getCreatedAt())
        .updatedAt(notification2.getUpdatedAt())
        .confirmed(notification2.getIsConfirmed())
        .userId(notification2.getUserId().toString())
        .content(notification2.getContent())
        .resourceType(notification2.getResource_type().name())
        .resourceId(notification2.getResourceId().toString())
        .build();

    given(notificationRepository.findUnreadByCursor(eq(userId), any(), any(), eq(3))).willReturn(
        mockList);
    given(notificationMapper.toDto(notification1)).willReturn(dto1);
    given(notificationMapper.toDto(notification2)).willReturn(dto2);
    given(notificationRepository.countByUserIdAndIsConfirmedFalse(userId)).willReturn(2);

    CursorPageResponseNotificationDto response = notificationService.getUnreadNotifications(
        userId.toString(), null, null, 2);

    assertThat(response.getContent()).hasSize(2);
    assertThat(response.getContent().get(0).getContent()).isEqualTo("알림1");
    assertThat(response.getContent().get(1).getContent()).isEqualTo("알림2");
    assertThat(response.isHasNext()).isFalse();
  }

  @Test
  void 알림_삭제() {
    notificationService.deleteNotification();
    verify(notificationRepository)
        .deleteByIsConfirmedTrueAndUpdatedAtBefore(any(LocalDateTime.class));
  }

  @Test
  void 읽지_않은_알림_조회_빈결과() {
    given(notificationRepository.findUnreadByCursor(eq(userId), any(), any(), eq(3)))
        .willReturn(List.of());
    given(notificationRepository.countByUserIdAndIsConfirmedFalse(userId)).willReturn(0);

    CursorPageResponseNotificationDto response = notificationService.getUnreadNotifications(
        userId.toString(), null, null, 2);

    assertThat(response.getContent()).isEmpty();
    assertThat(response.isHasNext()).isFalse();
  }

  @Test
  void 존재하지_않는_댓글_좋아요_알림_생성_실패() {
    UUID commentId = resourceId;
    given(commentRepository.findById(eq(commentId))).willReturn(Optional.empty());

    assertThrows(CommentNotFoundException.class, () -> {
      notificationService.createCommentLikeNotification(userId, commentId, "홍길동");
    });
  }

  @Test
  void 존재하지_않는_알림_확인_실패() {
    given(notificationRepository.findByIdAndUserId(eq(resourceId), eq(userId)))
        .willReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> {
      notificationService.confirmNotification(resourceId.toString(), userId.toString());
    });
  }
}
