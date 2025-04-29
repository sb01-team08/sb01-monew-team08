package com.example.monewteam08.exception.Subscription;

import com.example.monewteam08.exception.ErrorCode;

public class AlreadySubscribedException extends SubscriptionException {

  public AlreadySubscribedException() {
    super(ErrorCode.ALREADY_SUBSCRIPTION);
  }
}
