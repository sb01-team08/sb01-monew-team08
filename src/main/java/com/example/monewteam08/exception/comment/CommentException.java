package com.example.monewteam08.exception.comment;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;

public class CommentException extends MonewException {

  public CommentException(ErrorCode errorCode) {
    super(errorCode);
  }
}
