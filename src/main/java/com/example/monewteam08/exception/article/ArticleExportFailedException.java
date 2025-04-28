package com.example.monewteam08.exception.article;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;
import java.util.Map;

public class ArticleExportFailedException extends MonewException {

  public ArticleExportFailedException(String message) {
    super(ErrorCode.ARTICLE_EXPORT_FAILED, Map.of("message", message));
  }
}
