package com.example.monewteam08.batch;

import com.example.monewteam08.service.Interface.NotificationService;
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
public class deleteNotificationsJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final NotificationService notificationService;

  @Bean
  public Job deleteNotificationsJob(BatchJobMetricsListener batchJobMetricsListener) {
    return new JobBuilder("deleteNotificationsJob", jobRepository)
        .start(deleteNotificationsStep())
        .listener(batchJobMetricsListener)
        .build();
  }

  @Bean
  public Step deleteNotificationsStep() {
    return new StepBuilder("deleteNotificationsStep", jobRepository)
        .tasklet((contribution, chunkContext) -> {
          notificationService.deleteNotification();
          return RepeatStatus.FINISHED;
        }, transactionManager)
        .build();
  }

}
