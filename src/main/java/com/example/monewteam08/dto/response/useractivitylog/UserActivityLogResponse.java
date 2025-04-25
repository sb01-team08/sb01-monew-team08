package com.example.monewteam08.dto.response.useractivitylog;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

// todo: List<Object>는 추후에 수정할 것
@Builder
public record UserActivityLogResponse(
    @NotBlank UUID id, @NotBlank String email, @NotBlank String nickname,
    @NotBlank LocalDateTime createdAt,
    List<UserActivitySubscriptionResponse> subscriptions,
    List<CommentRecentLogResponse> comments,
    List<CommentLikeLogResponse> commentLikes,
    List<NewsViewLogResponse> articleViews) {

}
