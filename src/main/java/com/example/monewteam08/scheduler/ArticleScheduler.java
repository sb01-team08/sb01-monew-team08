package com.example.monewteam08.scheduler;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.exception.article.ArticleFetchAndSaveJobFailedException;
import com.example.monewteam08.exception.article.ArticleFetchFailedException;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.ArticleBackupService;
import com.example.monewteam08.service.Interface.ArticleService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleScheduler {

  private final JobLauncher jobLauncher;
  private final Job fetchAndSaveArticlesJob;

  private final ArticleService articleService;
  private final ArticleBackupService articleBackupService;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;

  @Scheduled(cron = "0 0 * * * *")
  public void runFetchAndSaveJob() {
    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addLong("timestamp", System.currentTimeMillis())
          .toJobParameters();

      jobLauncher.run(fetchAndSaveArticlesJob, jobParameters);
    } catch (Exception e) {
      throw new ArticleFetchAndSaveJobFailedException();
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

  @Scheduled(cron = "0 0 1 * * *")
  public void deleteOldArticles() {
    LocalDateTime cutoffDate = LocalDate.now().minusDays(3).atStartOfDay();
    List<Article> oldArticles = articleRepository.deleteAllByPublishDateBefore(cutoffDate);
    log.info("Deleted {} old articles", oldArticles.size());
  }
}
