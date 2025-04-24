package com.example.monewteam08.exception.Subscription;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.Interest.InterestException;

public class AlreadySubscribedException extends InterestException {

  public AlreadySubscribedException() {
    super(ErrorCode.ALREADY_SUBSCRIPTION);
  }
}
