package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.entity.SubscriptionMLog;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.exception.Interest.InterestNotFoundException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.SubscriptionMLogMapper;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.service.Interface.SubscriptionMLogService;
import com.mongodb.BasicDBObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionMLogServiceImpl implements SubscriptionMLogService {

  private final InterestRepository interestRepository;
  private final SubscriptionMLogMapper subscriptionMLogMapper;
  private final UserActivityMongoRepository userActivityMongoRepository;

  private final MongoTemplate mongoTemplate;

  @Override
  public void addSubscriptionLog(UUID userId, Subscription subscription, Interest interest) {
    UserActivity userActivity = userActivityMongoRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    SubscriptionMLog subscriptionMLog = subscriptionMLogMapper.toEntity(interest, subscription);

    List<SubscriptionMLog> subscriptions = userActivity.getSubscriptions();
    subscriptions.add(0, subscriptionMLog);

    userActivity.updateSubscriptions(subscriptions);
    userActivityMongoRepository.save(userActivity);
    log.info("관심사 로그 추가 완료: userId={}", userId);
  }

  @Override
  public void removeSubscriptionLog(UUID interestId) {
    // 모든 userActivity 문서 중 subscriptions 배열 안에 해당 interestId가 있는 도큐먼트 찾기
    Query query = Query.query(Criteria.where("subscriptions.interestId").is(interestId));

    // 조건에 맞는 subscriptions 배열 요소 삭제
    BasicDBObject pullCondition = new BasicDBObject("interestId", interestId);
    Update update = new Update().pull("subscriptions", pullCondition);

    // 여러 도큐먼트에 적용되도록 updateMulti 실행
    mongoTemplate.updateMulti(query, update, UserActivity.class);
  }

  @Override
  public List<UserActivitySubscriptionResponse> getSubscriptionLogs(
      List<SubscriptionMLog> subscriptionMLogs) {
    Set<UUID> interestIds = subscriptionMLogs.stream()
        .map(SubscriptionMLog::getInterestId)
        .collect(Collectors.toSet());

    Map<UUID, Interest> interestMap = interestRepository.findByIdIn(interestIds)
        .stream()
        .collect(Collectors.toMap(Interest::getId, Function.identity()));

    return subscriptionMLogs.stream()
        .map(subscriptionMLog -> {
          Interest interest = interestMap.get(subscriptionMLog.getInterestId());
          if (interest == null) {
            throw new InterestNotFoundException(subscriptionMLog.getInterestId().toString());
          }
          return subscriptionMLogMapper.toResponse(subscriptionMLog, interest);
        })
        .collect(Collectors.toList());
  }
}
