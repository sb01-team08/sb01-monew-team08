package com.example.monewteam08.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.entity.ResourceType;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class NotificationRepositoryCustomImplTest {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private NotificationRepositoryCustomImpl notificationRepositoryCustom;

  @Autowired
  private EntityManager em;

  private UUID userId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
  }

  @Test
  void 커서없이_읽지않은_알림_조회() {
    for (int i = 0; i < 5; i++) {
      Notification notification = new Notification(
          userId, "알림" + i, ResourceType.COMMENT, UUID.randomUUID()
      );
      notificationRepository.save(notification);
    }
    em.flush();
    em.clear();

    List<Notification> result = notificationRepositoryCustom.findUnreadByCursor(userId,
        null, null, 10);

    assertThat(result).hasSize(5);
    assertThat(result).allMatch(n -> !n.getIsConfirmed());
  }


  @Test
  void 커서로_읽지않은_알림_조회() {
    LocalDateTime now = LocalDateTime.now();

    for (int i = 0; i < 5; i++) {
      Notification notification = new Notification(
          userId, "알림 " + i, ResourceType.COMMENT, UUID.randomUUID()
      );
      ReflectionTestUtils.setField(notification, "createdAt", now.minusMinutes(i));
      notificationRepository.save(notification);
    }
    em.flush();
    em.clear();

    LocalDateTime cursor = now.minusMinutes(2);

    List<Notification> result = notificationRepositoryCustom.findUnreadByCursor(userId, cursor,
        null, 10);

    boolean hasNext = result.size() > 2;
    if (hasNext) {
      result = result.subList(0, 2);
    }

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getContent()).contains("알림 3");
    assertThat(result.get(1).getContent()).contains("알림 4");
  }

  @Test
  void 읽지않은_알림만_조회된다() {
    Notification unreadNotification = new Notification(
        userId, "읽지 않은 알림", ResourceType.COMMENT, UUID.randomUUID()
    );

    Notification readNotification = new Notification(
        userId, "읽은 알림", ResourceType.COMMENT, UUID.randomUUID()
    );
    readNotification.confirm(true);

    notificationRepository.save(unreadNotification);
    notificationRepository.save(readNotification);
    em.flush();
    em.clear();

    List<Notification> result = notificationRepositoryCustom.findUnreadByCursor(userId, null, null,
        10);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getContent()).isEqualTo("읽지 않은 알림");
    assertThat(result.get(0).getIsConfirmed()).isFalse();
  }
}