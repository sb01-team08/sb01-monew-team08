package com.example.monewteam08.controller;

import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.service.Interface.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.startsWith(createdAt.toString().substring(0, 23))));
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


}