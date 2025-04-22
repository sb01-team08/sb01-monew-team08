package com.example.monewteam08.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.monewteam08.dto.request.user.UserLoginRequest;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.request.user.UserUpdateRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.service.Interface.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserContoller.class)
class UserContollerTest {

  @Autowired
  private MockMvc mockMvc;

  @InjectMocks
  private UserContoller userContoller;

  @MockitoBean
  private UserService userService;

  private final ObjectMapper objectMapper = new ObjectMapper(); // 직렬화 위함

  @Test
  @DisplayName("요청한 값이 유효하면 사용자를 생성한다.(회원 가입을 진행한다)")
  void createUserIfRequestValueIsValid() throws Exception {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234!";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserRequest userRequest = new UserRequest(email, nickname, password);
    UserResponse expectResponse = new UserResponse(id, email, nickname, createdAt);

    given(userService.create(userRequest)).willReturn(expectResponse);

    // when
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(id.toString()))
        .andExpect(jsonPath("$.data.email").value(email))
        .andExpect(jsonPath("$.data.nickname").value(nickname))
        .andExpect(jsonPath("$.data.createdAt").value(
            Matchers.startsWith(createdAt.toString().substring(0, 23))));
  }

  @Test
  @DisplayName("Email 형식에 맞추지 않았을 때 Validation Error를 리턴한다.")
  void failedCreateUserIfEmailIsNotValid() throws Exception {
    // given
    String email = "test@mail";
    String nickname = "tester";
    String password = "testPassword1234!";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserRequest userRequest = new UserRequest(email, nickname, password);
    UserResponse expectResponse = new UserResponse(id, email, nickname, createdAt);

    given(userService.create(userRequest)).willReturn(expectResponse);

    // when
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
        .andExpect(jsonPath("$.error.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  @Test
  @DisplayName("비밀번호 형식에 맞추지 않았을 때 Validation Error를 리턴한다.")
  void failedCreateUserIfPasswordIsNotValid() throws Exception {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testpass1";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserRequest userRequest = new UserRequest(email, nickname, password);
    UserResponse expectResponse = new UserResponse(id, email, nickname, createdAt);

    given(userService.create(userRequest)).willReturn(expectResponse);

    // when

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
        .andExpect(jsonPath("$.error.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  @Test
  @DisplayName("요청한 값이 유효하면 해당 사용자의 닉네임을 갱신한다.")
  void updateUserNickname_RequestIsValid() throws Exception {
    // given
    String email = "test@example.com";
    String changeNickname = "change";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserUpdateRequest updateRequest = new UserUpdateRequest("change");
    UserResponse expectResponse = new UserResponse(id, email, changeNickname, createdAt);

    given(userService.update(id, updateRequest)).willReturn(expectResponse);

    // when
    mockMvc.perform(patch("/api/users/{userId}", id)
            .header("MoNew-Request-User-ID", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(id.toString()))
        .andExpect(jsonPath("$.data.email").value(email))
        .andExpect(jsonPath("$.data.nickname").value(changeNickname))
        .andExpect(jsonPath("$.data.createdAt").value(
            Matchers.startsWith(createdAt.toString().substring(0, 23))));
  }

  @Test
  @DisplayName("nickname을 빈 값으로 전달받았을 때 Validation Error를 리턴한다.")
  void failedUpdateUserIfNicknameIsNotValid() throws Exception {
    // given
    String email = "test@mail";
    String changeNickname = "change";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserUpdateRequest updateRequest = new UserUpdateRequest("");
    UserResponse expectResponse = new UserResponse(id, email, changeNickname, createdAt);

    given(userService.update(id, updateRequest)).willReturn(expectResponse);

    // when
    mockMvc.perform(patch("/api/users/{userId}", id)
            .header("MoNew-Request-User-ID", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
        .andExpect(jsonPath("$.error.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  @Test
  @DisplayName("요청한 값이 유효하면 해당 사용자의 논리 삭제를 수행한다.")
  void deleteUser_RequestIsValid() throws Exception {
    // given
    UUID id = UUID.randomUUID();

    willDoNothing().given(userService).delete(id);

    // when
    mockMvc.perform(delete("/api/users/{userId}", id)
            .header("MoNew-Request-User-ID", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("요청한 값이 유효하면 해당 사용자의 물리 삭제를 수행한다.")
  void hardDeleteUser_RequestIsValid() throws Exception {
    // given
    UUID id = UUID.randomUUID();

    willDoNothing().given(userService).hardDelete(id);

    // when
    mockMvc.perform(delete("/api/users/{userId}/hard", id)
            .header("MoNew-Request-User-ID", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("요청한 값이 유효하면 로그인을 진행한다")
  void loginUser_RequestValueIsValid() throws Exception {
    // given
    String email = "test@example.com";
    String nickname = "tester";
    String password = "testPassword1234!";
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserLoginRequest userRequest = new UserLoginRequest(email, password);
    UserResponse expectResponse = new UserResponse(id, email, nickname, createdAt);

    given(userService.login(userRequest)).willReturn(expectResponse);

    // when
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(id.toString()))
        .andExpect(jsonPath("$.data.email").value(email))
        .andExpect(jsonPath("$.data.nickname").value(nickname))
        .andExpect(jsonPath("$.data.createdAt").value(
            Matchers.startsWith(createdAt.toString().substring(0, 23))));
  }

}