package com.example.monewteam08.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class UserTest {

    @Test
    @DisplayName("유저 객체가 성공적으로 생성된다.")
    void userEntityCreateSuccess() {
        // given
        String email = "test@example.com";
        String nickname = "tester";
        String password = "testPassword1234";
        // when
        User user = new User(email, nickname, password);

        // then
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getEmail()).isEqualTo(email);
        Assertions.assertThat(user.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(user.getPassword()).isEqualTo(password);
        Assertions.assertThat(user.isActive()).isTrue();
        Assertions.assertThat(user.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("사용자 객체의 닉네임을 성공적으로 수정한다.")
    void userUpdateNicknameSuccess() {
        // given
        String email = "test@example.com";
        String nickname = "tester";
        String password = "testPassword1234";

        User user = new User(email, nickname, password);

        String updateNickname = "updateNickname";

        // when
        user.updateNickname(updateNickname);

        // then
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getNickname()).isEqualTo(updateNickname);
    }

}