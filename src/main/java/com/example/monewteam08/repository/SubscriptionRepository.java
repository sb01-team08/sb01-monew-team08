package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Subscription;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

  Optional<Subscription> findByUserIdAndInterestId(UUID userId, UUID interestId);

  void deleteByUserIdAndInterestId(UUID userId, UUID interestId);
}
