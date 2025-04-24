package com.example.monewteam08.dto.response.useractivitylog;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// todo: List<Object>는 추후에 수정할 것
public record UserActivityResponse(
    @NotBlank UUID id, @NotBlank String email, @NotBlank String nickname,
    @NotBlank LocalDateTime createdAt,
    List<Object> subscriptions, List<Object> comments, List<Object> commentLikes,
    List<Object> articleViews) {

}
