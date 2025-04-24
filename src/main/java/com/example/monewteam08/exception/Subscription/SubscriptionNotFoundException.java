package com.example.monewteam08.exception.Subscription;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.Interest.InterestException;

public class SubscriptionNotFoundException extends InterestException {

  public SubscriptionNotFoundException() {
    super(ErrorCode.SUBSCRIPTION_NOT_FOUND);
  }
}
