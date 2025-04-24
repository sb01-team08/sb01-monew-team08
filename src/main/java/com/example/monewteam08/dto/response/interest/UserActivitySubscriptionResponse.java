package com.example.monewteam08.dto.response.interest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserActivitySubscriptionResponse(UUID id, UUID interestId, String interestName,
                                               List<String> interestKeywords,
                                               int interestSubscriberCount,
                                               LocalDateTime createdAt) {

}
