package com.example.monewteam08.exception.Interest;

import com.example.monewteam08.exception.ErrorCode;
import java.util.Map;

public class DuplicateInterestException extends InterestException {

  public DuplicateInterestException(String name) {
    super(ErrorCode.DUPLICATE_INTEREST, Map.of("InterestName", name));
  }
}
