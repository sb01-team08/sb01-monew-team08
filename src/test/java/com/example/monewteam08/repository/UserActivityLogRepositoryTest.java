package com.example.monewteam08.repository;

import com.example.monewteam08.config.JacConfig;
import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import({JacConfig.class, QuerydslConfig.class})
class UserActivityLogRepositoryTest {

  @Autowired
  private UserActivityLogRepository userActivityLogRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("사용자 활동 내역 테이블이 존재하면 불러온다.")
  void getUserActivityLog() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);
    userRepository.saveAndFlush(user);

    UserActivityLog saveActivity = new UserActivityLog(user);
    userActivityLogRepository.save(saveActivity);

    // when
    Optional<UserActivityLog> userActivityLog = userActivityLogRepository.findByUserId(
        user.getId());

    // then
    Assertions.assertThat(userActivityLog).isPresent();
  }

  @Test
  @DisplayName("사용자 활동 내역 테이블이 존재하지 않으면 빈 값을 반환한다.")
  void canNotLoadUserActivityLog() {
    // given
    UUID userId = UUID.randomUUID();
    // when
    Optional<UserActivityLog> userActivityLog = userActivityLogRepository.findByUserId(userId);
    // then
    Assertions.assertThat(userActivityLog).isNotPresent();
  }


}