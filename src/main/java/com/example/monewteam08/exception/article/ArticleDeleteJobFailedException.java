package com.example.monewteam08.exception.article;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;

public class ArticleDeleteJobFailedException extends MonewException {

  public ArticleDeleteJobFailedException() {
    super(ErrorCode.ARTICLE_DELETE_JOB_FAILED);
  }
}
