package com.example.monewteam08.exception.comment;

import com.example.monewteam08.exception.ErrorCode;

public class UnauthorizedCommentAccessException extends CommentException {

  public UnauthorizedCommentAccessException() {
    super(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
  }
}
