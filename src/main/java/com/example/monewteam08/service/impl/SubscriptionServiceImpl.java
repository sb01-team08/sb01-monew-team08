package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.interest.UserActivitySubscriptionResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.exception.Interest.InterestNotFoundException;
import com.example.monewteam08.exception.Subscription.AlreadySubscribedException;
import com.example.monewteam08.exception.Subscription.SubscriptionNotFoundException;
import com.example.monewteam08.mapper.SubscriptionMapper;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.SubscriptionRepository;
import com.example.monewteam08.service.Interface.SubscriptionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final InterestRepository interestRepository;
  private final SubscriptionMapper subscriptionMapper;

  @Override
  public void subscribe(UUID userId, UUID interestId) {
    boolean alreadySubscribed = subscriptionRepository.findByUserIdAndInterestId(userId, interestId)
        .isPresent();

    if (alreadySubscribed) {
      throw new AlreadySubscribedException();
    }

    Interest interest = interestRepository.findById(interestId)
        .orElseThrow(() -> new InterestNotFoundException(interestId.toString()));

    Subscription subscription = new Subscription(userId, interestId);
    subscriptionRepository.save(subscription);

    interest.increaseSubscriberCount();
    interestRepository.save(interest);
  }

  @Override
  public void unsubscribe(UUID userId, UUID interestId) {
    Subscription sub = subscriptionRepository.findByUserIdAndInterestId(userId, interestId)
        .orElseThrow(SubscriptionNotFoundException::new);

    Interest interest = interestRepository.findById(interestId)
        .orElseThrow(() -> new InterestNotFoundException(interestId.toString()));

    subscriptionRepository.delete(sub);

    interest.decreaseSubscriberCount();
    interestRepository.save(interest);
  }

  @Override
  public List<UserActivitySubscriptionResponse> getSubscribeForUserActivity(UUID userId) {
    List<Subscription> subscriptionList = subscriptionRepository.findSubscriptionsByUserId(userId);

    return subscriptionList.stream()
        .map(subscription -> {
          Interest interest = interestRepository.findById(subscription.getInterestId()).orElseThrow(
              () -> new InterestNotFoundException(subscription.getInterestId().toString()));
          return subscriptionMapper.toResponse(subscription, interest);
        })
        .toList();
  }
}
