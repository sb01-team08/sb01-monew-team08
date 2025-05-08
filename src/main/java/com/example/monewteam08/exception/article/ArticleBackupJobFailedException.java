package com.example.monewteam08.exception.article;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;

public class ArticleBackupJobFailedException extends MonewException {

  public ArticleBackupJobFailedException() {
    super(ErrorCode.ARTICLE_BACKUP_JOB_FAILED);
  }
}
