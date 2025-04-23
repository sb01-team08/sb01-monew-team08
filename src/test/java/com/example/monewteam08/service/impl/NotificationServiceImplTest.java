package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.nodtification.NotificationDto;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.mapper.NotificationMapper;
import com.example.monewteam08.repository.NotificationRepository;
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
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

  @Mock
  private NotificationRepository notificationRepository;

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
    notificationService.createCommentLikeNotification(userId, resourceId, "홍길동" );

    verify(notificationRepository).save(any(Notification.class));
  }

  @Test
  void 알림_단건_확인() {
    Notification mock = mock(Notification.class);
    given(notificationRepository.findByIdAndUserId(resourceId, userId)).willReturn(
        Optional.of(mock));

    notificationService.confirmNotification(resourceId, userId);

    verify(mock).confirm();
  }

  @Test
  void 미확인_알림_조회() {
    LocalDateTime cursor = LocalDateTime.now();
    List<Notification> mockResult = List.of(mock(Notification.class));
    given(notificationRepository.findUnreadByUserIdBefore(eq(userId), eq(cursor),
        any(PageRequest.class))).willReturn(mockResult);
    given(notificationMapper.toDto(any())).willReturn(mock(NotificationDto.class));

    List<NotificationDto> result = notificationService.getUnreadNotifications(userId,
        cursor, 5);

    assertThat(result).hasSize(1);
    verify(notificationRepository)
        .findUnreadByUserIdBefore(eq(userId), eq(cursor), any(PageRequest.class));
  }

  @Test
  void 알림_삭제() {
    notificationService.deleteNotification();

    verify(notificationRepository)
        .deleteByIsConfirmedTrueAndUpdatedAtBefore(any(LocalDateTime.class));
  }
}