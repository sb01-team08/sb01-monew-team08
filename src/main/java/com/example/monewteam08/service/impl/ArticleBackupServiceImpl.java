package com.example.monewteam08.service.impl;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.service.Interface.ArticleBackupService;
import com.example.monewteam08.service.Interface.CsvService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleBackupServiceImpl implements ArticleBackupService {

  private final ArticleRepository articleRepository;
  private final CsvService csvService;

  @Override
  public void backup() {
    LocalDate today = LocalDate.now();

    List<Article> articles = articleRepository.findByPublishDate(today);

    if (articles.isEmpty()) {
      log.info("No articles found for backup on {}", today);
      return;
    }

    String csvPath = "backup/articles_" + today + ".csv";
    csvService.exportArticlesToCsv(today, articles);

    // TODO: S3에 업로드
  }

  @Override
  public void restore() {

  }
}
