package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.exception.Subscription.AlreadySubscribedException;
import com.example.monewteam08.exception.Subscription.SubscriptionNotFoundException;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.SubscriptionRepository;
import com.example.monewteam08.service.Interface.SubscriptionMLogService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private InterestRepository interestRepository;

  @Mock
  private SubscriptionMLogService subscriptionMLogService;

  @InjectMocks
  private SubscriptionServiceImpl subscriptionService;


  @Test
  @DisplayName("관심사를 정상적으로 구독할 수 있다.")
  void subscribeSuccess() {
    //given
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    Interest interest = new Interest(interestId, "기후변화", List.of("지구온난화"), 0);
    when(interestRepository.findById(interestId)).thenReturn(Optional.of(interest));
    when(subscriptionRepository.findByUserIdAndInterestId(userId, interestId)).thenReturn(
        Optional.empty());

    //when
    subscriptionService.subscribe(userId, interestId);

    //then
    verify(subscriptionRepository).save(any(Subscription.class));
    verify(subscriptionMLogService).addSubscriptionLog(eq(userId), any(Subscription.class),
        eq(interest));
  }

  @Test
  @DisplayName("이미 구독한 관심사를 중복 구독하면 예외 발생")
  void subscribeDuplicateFail() {
    //given
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    Subscription existing = new Subscription(userId, interestId);
    when(subscriptionRepository.findByUserIdAndInterestId(userId, interestId)).thenReturn(
        Optional.of(existing));

    //when then
    assertThatThrownBy(() -> subscriptionService.subscribe(userId, interestId))
        .isInstanceOf(AlreadySubscribedException.class);
  }

  @Test
  @DisplayName("관심사 구독을 취소할 수 있다.")
  void unsubscribeSuccess() {
    //given
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    Subscription subscription = new Subscription(userId, interestId);
    Interest interest = new Interest(interestId, "환경보호", List.of("ESG"), 10);

    when(subscriptionRepository.findByUserIdAndInterestId(userId, interestId)).thenReturn(
        Optional.of(subscription));
    when(interestRepository.findById(interestId)).thenReturn(Optional.of(interest));

    //when
    subscriptionService.unsubscribe(userId, interestId);

    //then
    verify(subscriptionRepository).delete(subscription);
    verify(subscriptionMLogService).removeSubscriptionLog(interestId);
  }

  @Test
  @DisplayName("존재하지 않는 구독을 취소하면 예외 발생")
  void unsubscribeFail() {
    //given
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    when(subscriptionRepository.findByUserIdAndInterestId(userId, interestId)).thenReturn(
        Optional.empty());

    //when & then
    assertThatThrownBy(() -> subscriptionService.unsubscribe(userId, interestId))
        .isInstanceOf(SubscriptionNotFoundException.class);
  }
}
