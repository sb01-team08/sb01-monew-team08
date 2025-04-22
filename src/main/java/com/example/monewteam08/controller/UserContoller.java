package com.example.monewteam08.controller;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.controller.api.UserControllerDocs;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.service.Interface.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

}
