package com.example.monewteam08.exception.article;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;
import java.util.Map;
import java.util.UUID;

public class ArticleNotFoundException extends MonewException {

  public ArticleNotFoundException(UUID id) {
    super(ErrorCode.ARTICLE_NOT_FOUND, Map.of("id", id));
  }
}
