package com.example.monewteam08.controller.api;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사용자 관리", description = "사용자 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    CustomApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest);
}
