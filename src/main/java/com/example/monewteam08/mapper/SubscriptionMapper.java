package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

  public UserActivitySubscriptionResponse toResponse(Subscription subscription, Interest interest) {
    return UserActivitySubscriptionResponse.builder()
        .id(subscription.getId())
        .interestId(subscription.getInterestId())
        .interestName(interest.getName())
        .interestKeywords(interest.getKeywords())
        .interestSubscriberCount(interest.getSubscriberCount())
        .createdAt(subscription.getSubscriptionDate())
        .build();
  }
}
