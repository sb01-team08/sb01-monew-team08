package com.example.monewteam08.exception.Interest;

import com.example.monewteam08.exception.ErrorCode;
import java.util.Map;

public class InterestNotFoundException extends InterestException {

  public InterestNotFoundException(String id) {
    super(ErrorCode.INTEREST_NOT_FOUND, Map.of("InterestId", id));
  }
}
