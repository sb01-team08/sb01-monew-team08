package com.example.monewteam08.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(@NotBlank String nickname) {

}
