package com.example.monewteam08.exception.useractivitylog;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;
import java.util.Map;

public abstract class UserActivityLogException extends MonewException {

  public UserActivityLogException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserActivityLogException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
