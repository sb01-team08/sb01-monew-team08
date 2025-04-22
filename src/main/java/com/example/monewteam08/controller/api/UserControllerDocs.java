package com.example.monewteam08.controller.api;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.dto.request.user.UserLoginRequest;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.request.user.UserUpdateRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사용자 관리", description = "사용자 관련 API")
public interface UserControllerDocs {

  @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
  CustomApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest);

  @Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
  CustomApiResponse<UserResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest);

  @Operation(summary = "사용자 정보 수정", description = "사용자의 닉네임을 수정합니다.")
  CustomApiResponse<UserResponse> updateUser(
      @PathVariable UUID userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest);

  @Operation(summary = "사용자 논리 삭제", description = "사용자를 논리적으로 삭제합니다.")
  CustomApiResponse<Void> deleteUser(@PathVariable UUID userId);

  @Operation(summary = "사용자 물리 삭제", description = "사용자를 물리적으로 삭제합니다.")
  CustomApiResponse<Void> hardDeleteUser(@PathVariable UUID userId);
}
