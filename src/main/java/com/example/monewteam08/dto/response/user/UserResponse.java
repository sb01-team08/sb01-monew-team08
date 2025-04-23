package com.example.monewteam08.dto.response.user;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(@NotBlank UUID id, @NotBlank String email, @NotBlank String nickname,
                           @NotBlank LocalDateTime createdAt) {

}
