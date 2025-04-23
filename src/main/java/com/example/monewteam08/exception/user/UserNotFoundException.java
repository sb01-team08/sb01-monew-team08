package com.example.monewteam08.exception.user;

import com.example.monewteam08.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(UUID id) {
    super(ErrorCode.USER_NOT_FOUND, Map.of("userId", id));
  }
}
