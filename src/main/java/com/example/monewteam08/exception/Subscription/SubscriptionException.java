package com.example.monewteam08.exception.Subscription;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;
import java.util.Map;

public class SubscriptionException extends MonewException {

  protected SubscriptionException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected SubscriptionException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
