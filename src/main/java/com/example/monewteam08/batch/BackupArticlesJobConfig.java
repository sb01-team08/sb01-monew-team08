package com.example.monewteam08.batch;

import com.example.monewteam08.service.Interface.ArticleBackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BackupArticlesJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final ArticleBackupService articleBackupService;

  @Bean
  public Job backupArticlesJob(BatchJobMetricsListener batchJobMetricsListener) {
    return new JobBuilder("backupArticlesJob", jobRepository)
        .start(backupArticlesStep())
        .listener(batchJobMetricsListener)
        .build();
  }

  @Bean
  public Step backupArticlesStep() {
    return new StepBuilder("backupArticlesStep", jobRepository)
        .tasklet((contribution, chunkContext) -> {
          articleBackupService.backup();
          return RepeatStatus.FINISHED;
        }, transactionManager)
        .build();
  }
}
