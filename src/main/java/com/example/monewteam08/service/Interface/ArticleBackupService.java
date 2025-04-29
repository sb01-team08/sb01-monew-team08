package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.article.ArticleRestoreResultDto;
import java.time.LocalDateTime;

public interface ArticleBackupService {

  void backup();

  ArticleRestoreResultDto restore(LocalDateTime from, LocalDateTime to);

}
