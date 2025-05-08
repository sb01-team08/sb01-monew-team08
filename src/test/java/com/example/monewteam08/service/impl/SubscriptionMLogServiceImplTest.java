package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.entity.SubscriptionMLog;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.mapper.SubscriptionMLogMapper;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.mongodb.BasicDBObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SubscriptionMLogServiceImplTest {

  @InjectMocks
  private SubscriptionMLogServiceImpl subscriptionMLogService;

  @Mock
  private InterestRepository interestRepository;
  @Mock
  private SubscriptionMLogMapper subscriptionMLogMapper;
  @Mock
  private UserActivityMongoRepository userActivityMongoRepository;
  @Mock
  private MongoTemplate mongoTemplate;

  @Test
  @DisplayName("관심사 구독 로그 추가 성공")
  void addSubscriptionLog_success() {
    // given
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    Subscription subscription = new Subscription(userId, interestId);

    Interest interest = new Interest(interestId, "관심사명", List.of("1", "2"), 0);

    SubscriptionMLog log = SubscriptionMLog.builder()
        .interestId(interestId)
        .build();

    UserActivity userActivity = UserActivity.builder()
        .id(userId)
        .subscriptions(new ArrayList<>())
        .build();

    given(userActivityMongoRepository.findById(userId)).willReturn(Optional.of(userActivity));
    given(subscriptionMLogMapper.toEntity(interest, subscription)).willReturn(log);

    // when
    subscriptionMLogService.addSubscriptionLog(userId, subscription, interest);

    // then
    then(userActivityMongoRepository).should().save(argThat(saved ->
        saved.getSubscriptions().size() == 1 &&
            saved.getSubscriptions().get(0).getInterestId().equals(interestId)
    ));
  }

  @Test
  @DisplayName("관심사 로그 삭제 성공 - 모든 유저에서")
  void removeSubscriptionLog_success() {
    // given
    UUID interestId = UUID.randomUUID();

    // when
    subscriptionMLogService.removeSubscriptionLog(interestId);

    // then
    Query expectedQuery = Query.query(Criteria.where("subscriptions.interestId").is(interestId));
    Update expectedUpdate = new Update().pull("subscriptions",
        new BasicDBObject("interestId", interestId));

    verify(mongoTemplate).updateMulti(expectedQuery, expectedUpdate, UserActivity.class);
  }

  @Test
  @DisplayName("관심사 구독 로그 리스트 조회 성공")
  void getSubscriptionLogs_success() {
    // given
    UUID interestId = UUID.randomUUID();

    SubscriptionMLog log = SubscriptionMLog.builder()
        .interestId(interestId)
        .build();

    Interest interest = new Interest(interestId, "관심사명", List.of("1", "2"), 50);

    UserActivitySubscriptionResponse response = new UserActivitySubscriptionResponse(
        UUID.randomUUID(), interestId, interest.getName(),
        interest.getKeywords(), interest.getSubscriberCount(), LocalDateTime.now()
    );

    given(interestRepository.findByIdIn(Set.of(interestId))).willReturn(List.of(interest));
    given(subscriptionMLogMapper.toResponse(log, interest)).willReturn(response);

    // when
    List<UserActivitySubscriptionResponse> result =
        subscriptionMLogService.getSubscriptionLogs(List.of(log));

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(response);
  }


}