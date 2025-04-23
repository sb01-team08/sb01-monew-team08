package com.example.monewteam08.exception.user;

import com.example.monewteam08.exception.ErrorCode;

public class MissingUserIdRequestHeaderException extends UserException {

  public MissingUserIdRequestHeaderException() {
    super(ErrorCode.MISSING_USER_ID_HEADER);
  }
}
