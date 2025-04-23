package com.example.monewteam08.scheduler;

import com.example.monewteam08.exception.article.ArticleFetchFailedException;
import com.example.monewteam08.service.Interface.ArticleService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArticleScheduler {

  private final ArticleService articleService;

  public ArticleScheduler(ArticleService articleService) {
    this.articleService = articleService;
  }

  @PostConstruct
  public void init() {
    try {
      articleService.fetchAndSave();
    } catch (Exception e) {
      throw new ArticleFetchFailedException(e.getMessage());
    }
  }

  @Scheduled(cron = "0 0 * * * *")
  public void fetchAndSaveArticles() {
    try {
      articleService.fetchAndSave();
    } catch (Exception e) {
      throw new ArticleFetchFailedException(e.getMessage());
    }
  }
}
