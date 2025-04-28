package com.example.monewteam08.scheduler;

import com.example.monewteam08.exception.article.ArticleFetchFailedException;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.ArticleBackupService;
import com.example.monewteam08.service.Interface.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleScheduler {

  private final ArticleService articleService;
  private final ArticleBackupService articleBackupService;
  private final UserRepository userRepository;

  @Scheduled(cron = "0 0 * * * *")
  public void fetchAndSaveArticles() {
    try {
      userRepository.findAll().forEach(user -> {
        articleService.fetchAndSave(user.getId());
      });
    } catch (Exception e) {
      throw new ArticleFetchFailedException(e.getMessage());
    }
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void backupArticles() {
    try {
      articleBackupService.backup();
    } catch (Exception e) {
      throw new ArticleFetchFailedException(e.getMessage());
    }
  }
}
