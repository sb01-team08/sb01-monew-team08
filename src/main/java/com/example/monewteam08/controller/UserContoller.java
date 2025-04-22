package com.example.monewteam08.controller;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.controller.api.UserControllerDocs;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.request.user.UserUpdateRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.service.Interface.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserContoller implements UserControllerDocs {

  private final UserService userService;

  @PostMapping
  public CustomApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
    return CustomApiResponse.create(userService.create(userRequest));
  }

  @PatchMapping("/{userId}")
  public CustomApiResponse<UserResponse> updateUser(
      @RequestHeader("MoNew-Request-User-ID") UUID userRequestId,
      @PathVariable UUID userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
    return CustomApiResponse.ok(userService.update(userId, userUpdateRequest));
  }

  @DeleteMapping("/{userId}")
  public void deleteUser(
      @RequestHeader("MoNew-Request-User-ID") UUID userRequestId, @PathVariable UUID userId) {
    userService.delete(userId);
  }

}
