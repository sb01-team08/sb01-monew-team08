package com.example.monewteam08.repository;

import com.example.monewteam08.config.JacConfig;
import com.example.monewteam08.entity.User;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import(JacConfig.class)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("User 객체를 성공적으로 저장한다.")
  void saveUserSuccess() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);

    // when
    User saved = userRepository.save(user);

    // then
    Assertions.assertThat(saved).isNotNull();
    Assertions.assertThat(saved.getNickname()).isEqualTo(nickname);
    Assertions.assertThat(saved.getCreatedAt()).isNotNull();
  }

  @Test
  @DisplayName("User 객체를 UUID를 통해 성공적으로 불러온다.")
  void findUserByIdSuccess() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);
    User saved = userRepository.save(user);

    // when
    User getUser = userRepository.findById(saved.getId()).orElseThrow();

    // then
    Assertions.assertThat(getUser).isNotNull();
    Assertions.assertThat(getUser.getId()).isEqualTo(saved.getId());
    Assertions.assertThat(getUser.getNickname()).isEqualTo(saved.getNickname());
    Assertions.assertThat(getUser.getCreatedAt()).isNotNull();
  }

  @Test
  @DisplayName("사용자의 이메일이 이미 존재하면 true를 반환한다.")
  void existUserByEmail() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);
    userRepository.save(user);

    // when
    boolean isExist = userRepository.existsUserByEmail(email);

    // then
    Assertions.assertThat(isExist).isTrue();
  }

  @Test
  @DisplayName("사용자의 이메일이 존재하지 않으면 false를 반환한다.")
  void isNotExistUserByEmail() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);
    userRepository.save(user);

    // when
    boolean isExist = userRepository.existsUserByEmail("notExistEmail@example.com");

    // then
    Assertions.assertThat(isExist).isFalse();
  }

  @Test
  @DisplayName("이메일과 비밀번호가 일치하는 활성화 된 사용자가 존재하면 해당 사용자를 리턴한다.")
  void isExistUserByEmailAndPassword() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);
    userRepository.save(user);

    // when
    User correctUser = userRepository.findUserByEmailAndPassword(email, password);

    // then
    Assertions.assertThat(correctUser).isNotNull();
    Assertions.assertThat(correctUser.getEmail()).isEqualTo(email);
  }

  @Test
  @DisplayName("이메일 혹은 비밀번호가 일치하지 않으면 Null 값을 리턴한다.")
  void isNotExistUserByEmailAndPassword() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);
    userRepository.save(user);

    // when
    User correctUser = userRepository.findUserByEmailAndPassword(email, "password1234!");

    // then
    Assertions.assertThat(correctUser).isNull();
  }

  @Test
  @DisplayName("활성화 된 유저가 아니라면 Null 값을 리턴한다.")
  void isNotActiveUser() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);
    user.updateIsActive(false);
    user.updateDeletedAt(LocalDateTime.now());
    userRepository.save(user);

    // when
    User correctUser = userRepository.findUserByEmailAndPassword(email, "password1234!");

    // then
    Assertions.assertThat(correctUser).isNull();
  }

}