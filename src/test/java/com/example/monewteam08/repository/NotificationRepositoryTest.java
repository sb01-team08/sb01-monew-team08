package com.example.monewteam08.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Notification;
import com.example.monewteam08.entity.ResourceType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class NotificationRepositoryTest {

  @Autowired
  private NotificationRepository notificationRepository;

  @Test
  void 알림_단건_조회() {
    UUID userId = UUID.randomUUID();
    Notification saved = notificationRepository.save(
        new Notification(userId, "알림", ResourceType.COMMENT, UUID.randomUUID()));

    Optional<Notification> result = notificationRepository.findByIdAndUserId(saved.getId(),
        userId);

    assertThat(result).isPresent();
    assertThat(result.get().getContent()).isEqualTo("알림");
  }

  @Test
  void 확인되지_않는_알람_전체_조회() {
    UUID userId = UUID.randomUUID();
    notificationRepository.save(
        new Notification(userId, "알림", ResourceType.COMMENT, UUID.randomUUID()));

    List<Notification> result = notificationRepository.findAllByUserIdAndIsConfirmedFalse(
        userId);

    assertThat(result).isNotEmpty();
    assertThat(result.size()).isEqualTo(1);
    assertThat(result.get(0).getIsConfirmed()).isFalse();
  }

  @Test
  void seven일_이전의_확인된_알림_삭제() {
    UUID userId = UUID.randomUUID();
    Notification confirmed7 = new Notification(userId, "알림", ResourceType.COMMENT,
        UUID.randomUUID());
    confirmed7.confirm();
    ReflectionTestUtils.setField(confirmed7, "updatedAt", LocalDateTime.now().minusDays(8));
    notificationRepository.save(confirmed7);

    notificationRepository.deleteByIsConfirmedTrueAndUpdatedAtBefore(
        LocalDateTime.now().minusDays(7));

    List<Notification> result = notificationRepository.findAll();
    assertThat(result).isEmpty();
  }

}