package com.example.monewteam08.scheduler;

import com.example.monewteam08.exception.article.ArticleBackupJobFailedException;
import com.example.monewteam08.exception.article.ArticleDeleteJobFailedException;
import com.example.monewteam08.exception.article.ArticleFetchAndSaveJobFailedException;
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
  private final Job backupArticlesJob;
  private final Job deleteOldArticlesJob;

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
  public void runBackupJob() {
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
    try {
      jobLauncher.run(backupArticlesJob, params);
    } catch (Exception e) {
      throw new ArticleBackupJobFailedException();
    }
  }

  @Scheduled(cron = "0 0 1 * * *")
  public void runDeleteOldArticlesJob() {
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
    try {
      jobLauncher.run(deleteOldArticlesJob, params);
    } catch (Exception e) {
      throw new ArticleDeleteJobFailedException();
    }
  }
}
