package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import java.util.List;
import java.util.UUID;

public interface SubscriptionService {

  //구독
  void subscribe(UUID userId, UUID interestId);

  //구독 취소
  void unsubscribe(UUID userId, UUID interestId);

  // 사용자 활동 내역 관심사 구독 정보 전달
  List<UserActivitySubscriptionResponse> getSubscribeForUserActivity(UUID userId);
}
