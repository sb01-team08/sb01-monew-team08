package com.example.monewteam08.service.Interface;

import java.util.UUID;

public interface SubscriptionService {

  //구독
  void subscribe(UUID userId, UUID interestId);

  //구독 취소
  void unsubscribe(UUID userId, UUID interestId);
}
