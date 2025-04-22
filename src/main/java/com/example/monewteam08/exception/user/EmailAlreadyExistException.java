package com.example.monewteam08.exception.user;

import com.example.monewteam08.exception.ErrorCode;
import java.util.Map;

public class EmailAlreadyExistException extends UserException {

  public EmailAlreadyExistException(String email) {
    super(ErrorCode.EMAIL_IS_ALREADY_EXIST, Map.of("email", email));
  }
}
