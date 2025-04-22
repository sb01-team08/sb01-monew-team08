package com.example.monewteam08.exception.user;

import com.example.monewteam08.exception.ErrorCode;

public class InvalidUserIdRequestHeaderFormatException extends UserException {

  public InvalidUserIdRequestHeaderFormatException() {
    super(ErrorCode.INVALID_USER_ID_HEADER_FORMAT);
  }
}
