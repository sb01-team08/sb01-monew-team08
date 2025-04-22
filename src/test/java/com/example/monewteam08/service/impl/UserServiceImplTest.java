package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.request.user.UserLoginRequest;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.request.user.UserUpdateRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.user.DeletedAccountException;
import com.example.monewteam08.exception.user.EmailAlreadyExistException;
import com.example.monewteam08.exception.user.LoginFailedException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.UserMapper;
import com.example.monewteam08.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  @DisplayName("사용자를 성공적으로 생성한다.")
  void createUserSuccess() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserRequest userRequest = new UserRequest(email, nickname, password);
    User user = new User(email, nickname, password);

    User userAfterSave = new User(email, nickname, password);
    ReflectionTestUtils.setField(userAfterSave, "id", id);
    ReflectionTestUtils.setField(userAfterSave, "createdAt", createdAt);

    UserResponse expectResponse = new UserResponse(id, email, nickname, createdAt);

    given(userRepository.existsUserByEmail(email)).willReturn(false);
    given(userMapper.toEntity(userRequest)).willReturn(user);
    given(userRepository.save(user)).willReturn(userAfterSave);
    given(userMapper.toResponse(userAfterSave)).willReturn(expectResponse);

    // when
    UserResponse userResponse = userService.create(userRequest);

    // then
    Assertions.assertThat(userResponse).isNotNull();
    Assertions.assertThat(userResponse).isInstanceOf(UserResponse.class);

    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("이메일 중복으로 인하여 사용자 생성이 불가능하다.")
  void createUserFailed_causeEmailDuplicate() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    UserRequest userRequest = new UserRequest(email, "qwerty", "qwerty1234");

    given(userRepository.existsUserByEmail(email)).willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.create(userRequest))
        .isInstanceOf(EmailAlreadyExistException.class)
        .hasMessage(ErrorCode.EMAIL_IS_ALREADY_EXIST.getCode() + ": "
            + ErrorCode.EMAIL_IS_ALREADY_EXIST.getMessage());

  }

  @Test
  @DisplayName("성공적으로 닉네임을 변경한다")
  void updateUserSuccess() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    String changeNickname = "changeNickname";

    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(changeNickname);

    User user = new User(email, nickname, password);
    ReflectionTestUtils.setField(user, "id", id);
    ReflectionTestUtils.setField(user, "createdAt", createdAt);

    UserResponse expectResponse = new UserResponse(id, email, changeNickname, createdAt);

    given(userRepository.findById(id)).willReturn(Optional.of(user));
    given(userMapper.toResponse(user)).willReturn(expectResponse);

    // when
    UserResponse userResponse = userService.update(id, userUpdateRequest);

    // then
    Assertions.assertThat(userResponse).isNotNull();
    Assertions.assertThat(userResponse).isInstanceOf(UserResponse.class);
    Assertions.assertThat(userResponse.nickname()).isEqualTo(changeNickname);
  }

  @Test
  @DisplayName("수정 대상 사용자가 존재하지 않아 예외를 던진다.")
  void failedUpdateUser_UserNotFound() {
    // given
    UUID id = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest("changeNickname");

    given(userRepository.findById(id)).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> userService.update(id, updateRequest))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(
            ErrorCode.USER_NOT_FOUND.getCode() + ": " + ErrorCode.USER_NOT_FOUND.getMessage());
  }

  @Test
  @DisplayName("사용자 논리 삭제를 성공적으로 수행한다.")
  void deleteUser_softDelete() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    User user = new User(email, nickname, password);
    ReflectionTestUtils.setField(user, "id", id);
    ReflectionTestUtils.setField(user, "createdAt", createdAt);

    given(userRepository.findById(id)).willReturn(Optional.of(user));

    // when
    userService.delete(id);

    // then
    Assertions.assertThat(user.isActive()).isFalse();
    Assertions.assertThat(user.getDeletedAt()).isNotNull();
  }

  @Test
  @DisplayName("논리 삭제 대상 사용자가 존재하지 않아 예외를 던진다.")
  void failedDeleteUser_UserNotFound() {
    // given
    UUID id = UUID.randomUUID();

    given(userRepository.findById(id)).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> userService.delete(id))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(
            ErrorCode.USER_NOT_FOUND.getCode() + ": " + ErrorCode.USER_NOT_FOUND.getMessage());
  }

  @Test
  @DisplayName("사용자 물리 삭제를 성공적으로 수행한다.")
  void deleteUser_hardDelete() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234";

    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    User user = new User(email, nickname, password);
    ReflectionTestUtils.setField(user, "id", id);
    ReflectionTestUtils.setField(user, "createdAt", createdAt);

    given(userRepository.findById(id)).willReturn(Optional.of(user));

    // when
    userService.hardDelete(id);

    // then
    verify(userRepository).delete(user);
  }

  @Test
  @DisplayName("물리 삭제 대상 사용자가 존재하지 않아 예외를 던진다.")
  void failedDeleteUser_hard_delete_UserNotFound() {
    // given
    UUID id = UUID.randomUUID();

    given(userRepository.findById(id)).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> userService.hardDelete(id))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(
            ErrorCode.USER_NOT_FOUND.getCode() + ": " + ErrorCode.USER_NOT_FOUND.getMessage());
  }

  @Test
  @DisplayName("이메일과 비밀번호가 일치하여 로그인에 성공한다.")
  void loginUser_success() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "test!1234";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    User user = new User(email, nickname, password);
    ReflectionTestUtils.setField(user, "id", id);
    ReflectionTestUtils.setField(user, "createdAt", createdAt);

    UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
    UserResponse expectResponse = new UserResponse(id, email, nickname, createdAt);

    given(userRepository.findUserByEmailAndPassword(userLoginRequest.email(),
        userLoginRequest.password())).willReturn(user);

    given(userMapper.toResponse(user)).willReturn(expectResponse);

    // when
    UserResponse response = userService.login(userLoginRequest);

    // then
    Assertions.assertThat(response).isNotNull();
    Assertions.assertThat(response).isInstanceOf(UserResponse.class);
    Assertions.assertThat(response.nickname()).isEqualTo(expectResponse.nickname());
    Assertions.assertThat(response.email()).isEqualTo(expectResponse.email());
    Assertions.assertThat(response.id()).isEqualTo(expectResponse.id());
  }

  @Test
  @DisplayName("이메일 또는 비밀번호가 일치하지 않아 로그인에 실패한다.")
  void failedLoginUser_causeIncorrectValue() {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "test!1234";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    User user = new User(email, nickname, password);
    ReflectionTestUtils.setField(user, "id", id);
    ReflectionTestUtils.setField(user, "createdAt", createdAt);
    ReflectionTestUtils.setField(user, "isActive", false);

    UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);

    given(userRepository.findUserByEmailAndPassword(userLoginRequest.email(),
        userLoginRequest.password())).willReturn(user);

    // when & then
    Assertions.assertThatThrownBy(() -> userService.login(userLoginRequest))
        .isInstanceOf(LoginFailedException.class)
        .hasMessage(ErrorCode.LOGIN_FAILED.getCode() + ": " + ErrorCode.LOGIN_FAILED.getMessage());

  }

  @Test
  @DisplayName("삭제된 계정이라 로그인에 실패한다.")
  void failedLoginUser_causeUserIsInactive() {
    // given
    String email = "test@example.com";
    String wrongPassword = "password1234!";

    UserLoginRequest userLoginRequest = new UserLoginRequest(email, wrongPassword);

    given(userRepository.findUserByEmailAndPassword(userLoginRequest.email(),
        userLoginRequest.password())).willReturn(null);

    // when & then
    Assertions.assertThatThrownBy(() -> userService.login(userLoginRequest))
        .isInstanceOf(DeletedAccountException.class)
        .hasMessage(ErrorCode.LOGIN_REJECTED_DELETED_USER.getCode() + ": "
            + ErrorCode.LOGIN_REJECTED_DELETED_USER.getMessage());

  }

}