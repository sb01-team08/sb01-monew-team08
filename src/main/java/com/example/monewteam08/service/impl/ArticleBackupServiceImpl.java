package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.article.ArticleRestoreResultDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.service.Interface.ArticleBackupService;
import com.example.monewteam08.service.Interface.CsvService;
import com.example.monewteam08.service.Interface.S3Service;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleBackupServiceImpl implements ArticleBackupService {

  private final ArticleRepository articleRepository;
  private final CsvService csvService;
  private final S3Service s3Service;

  @Transactional
  @Override
  public void backup() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDateTime startOfDay = yesterday.atStartOfDay();
    LocalDateTime endOfDay = yesterday.plusDays(1).atStartOfDay();

    List<Article> articles = articleRepository.findAllByPublishDateBetween(startOfDay, endOfDay);

    if (articles.isEmpty()) {
      log.info("No articles found for backup on {}", yesterday);
      return;
    }

    String fileName = "articles_" + yesterday + ".csv";
    Path path = Path.of(System.getProperty("java.io.tmpdir"), fileName);

    csvService.exportArticlesToCsv(path, articles);
    log.info("Exported {} articles to {}", articles.size(), path);
    s3Service.upload(path, yesterday);
    log.info("Uploaded backup to S3: {}", fileName);
  }

  @Transactional
  @Override
  public ArticleRestoreResultDto restore(LocalDateTime from, LocalDateTime to) {
    List<Article> articles = new ArrayList<>();
    LocalDate dateFrom = from.toLocalDate();
    LocalDate dateTo = to.toLocalDate();

    Path csvPath;
    for (LocalDate date = dateFrom; !date.isAfter(dateTo); date = date.plusDays(1)) {
      csvPath = s3Service.download(date);
      List<Article> articlesFromCsv = csvService.importArticlesFromCsv(csvPath);
      if (articlesFromCsv.isEmpty()) {
        log.info("No articles found for date {}", date);
      }
      articles.addAll(articlesFromCsv);
      log.info("Imported {} articles from backup for date {}", articlesFromCsv.size(), date);
    }

    List<UUID> articleIds = articles.stream()
        .map(Article::getId)
        .distinct()
        .toList();
    Map<UUID, Article> existingArticles = articleRepository.findAllById(articleIds
    ).stream().collect(Collectors.toMap(Article::getId, article -> article));
    log.info("Found {} existing articles in the database", existingArticles.size());

    List<Article> lostArticles = new ArrayList<>();
    List<Article> activatedArticles = new ArrayList<>();

    for (Article article : articles) {
      Article existing = existingArticles.get(article.getId());

      if (existing == null) {
        Article newArticle = new Article(
            article.getSource(),
            article.getTitle(),
            article.getSummary(),
            article.getSourceUrl(),
            article.getPublishDate(),
            null
        );
        newArticle.setInterestId(article.getInterestId());
        lostArticles.add(newArticle);
        log.info("Article {} not found in the database, adding to lost articles", article.getId());
      } else if (!existing.isActive()) {
        existing.activate();
        activatedArticles.add(existing);
        log.info("Article {} activated", existing.getId());
      }
    }

    List<Article> restoredArticles = articleRepository.saveAll(lostArticles);
    log.info("Restored {} articles from backup", restoredArticles.size());
    articleRepository.saveAll(activatedArticles);
    log.info("Activated {} articles from backup", activatedArticles.size());

    for (Article article : restoredArticles) {
      log.debug("Restored article: {}", article);
    }
    for (Article article : activatedArticles) {
      log.debug("Activated article: {}", article);
    }
    return new ArticleRestoreResultDto(
        LocalDateTime.now(),
        restoredArticles.stream().map(Article::getId).toList(),
        restoredArticles.size()
    );
  }

}
