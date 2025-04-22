package com.example.monewteam08.exception.article;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;
import java.util.Map;

public class ArticleFetchFailedException extends MonewException {

  public ArticleFetchFailedException(String source) {
    super(ErrorCode.ARTICLE_FETCH_FAILED, Map.of("source", source));
  }
}
