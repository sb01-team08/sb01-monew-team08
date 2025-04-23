package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  private final UserMapper userMapper = new UserMapper();

  @Test
  @DisplayName("유저 객체를 받아 UserResponse를 성공적으로 생성한다")
  void createUserResponseToUser() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    User user = new User(email, nickname, password);

    // when
    UserResponse userResponse = userMapper.toResponse(user);

    // then
    Assertions.assertThat(userResponse).isNotNull();
    Assertions.assertThat(userResponse).isInstanceOf(UserResponse.class);
    Assertions.assertThat(userResponse.nickname()).isEqualTo(nickname);
    Assertions.assertThat(userResponse.email()).isEqualTo(email);
  }

  @Test
  @DisplayName("UserRequest 객체를 받아 User 객체를 성공적으로 생성한다.")
  void createUserToUserRequest() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    UserRequest userRequest = new UserRequest(email, nickname, password);

    // when
    User user = userMapper.toEntity(userRequest);

    // then
    Assertions.assertThat(user).isNotNull();
    Assertions.assertThat(user).isInstanceOf(User.class);
    Assertions.assertThat(user.getNickname()).isEqualTo(nickname);
    Assertions.assertThat(user.getPassword()).isEqualTo(password);
    Assertions.assertThat(user.getEmail()).isEqualTo(email);
  }

}