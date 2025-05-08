package com.example.monewteam08.exception.article;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;

public class ArticleFetchAndSaveJobFailedException extends MonewException {

  public ArticleFetchAndSaveJobFailedException() {
    super(ErrorCode.ARTICLE_FETCH_AND_SAVE_JOB_FAILED);
  }
}
