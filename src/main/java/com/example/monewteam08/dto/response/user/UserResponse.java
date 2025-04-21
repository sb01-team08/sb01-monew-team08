package com.example.monewteam08.dto.response.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse(@NotBlank UUID id, @NotBlank String email, @NotBlank String nickname,
                           @NotBlank LocalDateTime createdAt) {
}
