package com.example.monewteam08.exception.Subscription;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;

public class SubscriptionException extends MonewException {

  protected SubscriptionException(ErrorCode errorCode) {
    super(errorCode);
  }

}
