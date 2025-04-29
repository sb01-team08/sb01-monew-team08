package com.example.monewteam08.exception.Interest;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;
import java.util.Map;

public class InterestException extends MonewException {

  protected InterestException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected InterestException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
