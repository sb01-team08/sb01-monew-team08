package com.example.monewteam08.exception.user;

import com.example.monewteam08.exception.ErrorCode;

public class DeletedAccountException extends UserException {

  public DeletedAccountException() {
    super(ErrorCode.LOGIN_REJECTED_DELETED_USER);
  }
}
