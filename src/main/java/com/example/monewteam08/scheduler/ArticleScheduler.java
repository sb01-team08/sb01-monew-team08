package com.example.monewteam08.scheduler;

import com.example.monewteam08.service.Interface.ArticleService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ArticleScheduler {

  private final ArticleService articleService;

  public ArticleScheduler(ArticleService articleService) {
    this.articleService = articleService;
  }

  @PostConstruct
  public void init() {
    articleService.save();
  }

  @Scheduled(cron = "0 0 * * * *")
  public void fetchAndSaveArticles() {
    articleService.save();
  }
}
