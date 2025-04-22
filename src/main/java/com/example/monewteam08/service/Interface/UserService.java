package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.request.user.UserLoginRequest;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.request.user.UserUpdateRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import java.util.UUID;

public interface UserService {

  // 사용자 생성
  UserResponse create(UserRequest userRequest);

  // 정보 수정
  UserResponse update(UUID userId, UserUpdateRequest userUpdateRequest);

  // 논리 삭제
  void delete(UUID userId);

  // 물리 삭제
  void hardDelete(UUID userId);

  // 로그인
  UserResponse login(UserLoginRequest userLoginRequest);

}
