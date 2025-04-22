package com.example.monewteam08.scheduler;

import com.example.monewteam08.service.Interface.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleScheduler {

  private final ArticleService articleService;

  @Scheduled(cron = "0 0 * * * *")
  public void fetchAndSaveArticles() {
    articleService.save();
  }
}
