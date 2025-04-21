package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.response.user.UserResponse;

public interface UserService {

    // 사용자 생성
    UserResponse create(UserRequest userRequest);

    // 정보 수정

    // 논리 삭제

    // 물리 삭제

}
