package com.example.monewteam08.repository;

import com.example.monewteam08.config.MongoConfig;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import(MongoConfig.class)
class UserActivityMongoRepositoryTest {

  @Autowired
  private UserActivityMongoRepository userActivityMongoRepository;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User("test@example.com", "tester", "!tester1234");
  }

  @Test
  @DisplayName("사용자 활동 도큐먼트 저장을 성공적으로 수행한다.")
  void saveUserActivityDocumentSuccess() {
    // given
    UserActivity userActivity = UserActivity.builder()
        .id(UUID.randomUUID())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .build();

    // when
    userActivityMongoRepository.save(userActivity);
    Optional<UserActivity> result = userActivityMongoRepository.findById(userActivity.getId());
    // then
    Assertions.assertThat(result).isPresent();
    Assertions.assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
  }

}