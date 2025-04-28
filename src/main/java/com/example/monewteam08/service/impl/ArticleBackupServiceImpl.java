package com.example.monewteam08.service.impl;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.service.Interface.ArticleBackupService;
import com.example.monewteam08.service.Interface.CsvService;
import com.example.monewteam08.service.Interface.S3Service;
import java.nio.file.Path;
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
  private final S3Service s3Service;

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

    s3Service.upload(Path.of(csvPath), today);
  }

  @Override
  public void restore(LocalDate date) {
    Path csvPath = s3Service.download(date);
    List<Article> articles = csvService.importArticlesFromCsv(csvPath);

    List<Article> lostArticles = articles.stream()
        .filter(article -> articleRepository.findById(article.getId()).isEmpty()).toList();

    List<Article> restoredArticles = articleRepository.saveAll(lostArticles);

    log.info("Restored {} articles from backup for date {}", restoredArticles.size(), date);
    for (Article article : restoredArticles) {
      log.debug("Restored article: {}", article);
    }
  }
}
