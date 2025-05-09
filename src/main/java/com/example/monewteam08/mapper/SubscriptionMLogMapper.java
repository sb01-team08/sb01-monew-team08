package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.entity.SubscriptionMLog;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMLogMapper {

  public SubscriptionMLog toEntity(Interest interest, Subscription subscription) {
    return SubscriptionMLog.builder()
        .id(subscription.getId())
        .interestId(interest.getId())
        .build();
  }

  public UserActivitySubscriptionResponse toResponse(SubscriptionMLog subscription,
      Interest interest) {
    return UserActivitySubscriptionResponse.builder()
        .id(subscription.getId())
        .interestId(subscription.getInterestId())
        .interestName(interest.getName())
        .interestKeywords(interest.getKeywords())
        .interestSubscriberCount(interest.getSubscriberCount())
        .createdAt(subscription.getCreatedAt())
        .build();
  }
}
