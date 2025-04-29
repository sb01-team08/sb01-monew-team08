package com.example.monewteam08.exception.user;

import com.example.monewteam08.exception.ErrorCode;

public class LoginFailedException extends UserException {

  public LoginFailedException() {
    super(ErrorCode.LOGIN_FAILED);
  }
}
