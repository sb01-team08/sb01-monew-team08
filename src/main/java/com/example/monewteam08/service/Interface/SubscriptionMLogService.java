package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.entity.SubscriptionMLog;
import java.util.List;
import java.util.UUID;

public interface SubscriptionMLogService {

  // 관심사 구독 로그
  void addSubscriptionLog(UUID userId, Subscription subscription, Interest interest);

  void removeSubscriptionLog(UUID interestId);

  List<UserActivitySubscriptionResponse> getSubscriptionLogs(
      List<SubscriptionMLog> subscriptionMLogs);

}
