package com.example.monewteam08.exception.comment;

import com.example.monewteam08.exception.ErrorCode;

public class CommentNotFoundException extends CommentException {

  public CommentNotFoundException() {
    super(ErrorCode.COMMENT_NOT_FOUND);
  }
}
