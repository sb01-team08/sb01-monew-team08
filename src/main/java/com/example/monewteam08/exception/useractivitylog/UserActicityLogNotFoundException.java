package com.example.monewteam08.exception.useractivitylog;

import com.example.monewteam08.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserActicityLogNotFoundException extends UserActivityLogException {

  public UserActicityLogNotFoundException(UUID userId) {
    super(ErrorCode.USER_ACTIVITY_LOG_NOT_FOUND, Map.of("userId", userId));
  }
}
