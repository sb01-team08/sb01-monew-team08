package com.example.monewteam08.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.mapper.NotificationMapper;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.NotificationRepository;
import com.example.monewteam08.repository.NotificationRepositoryCustom;
import java.time.LocalDateTime;
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
  void 알림_삭제() {
    notificationService.deleteNotification();
    verify(notificationRepository)
        .deleteByIsConfirmedTrueAndUpdatedAtBefore(any(LocalDateTime.class));
  }
}
