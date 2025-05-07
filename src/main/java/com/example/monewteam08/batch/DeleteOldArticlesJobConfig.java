package com.example.monewteam08.batch;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
public class DeleteOldArticlesJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final ArticleRepository articleRepository;

  @Bean
  public Job deleteOldArticlesJob() {
    return new JobBuilder("deleteOldArticlesJob", jobRepository)
        .start(deleteOldArticlesStep())
        .build();
  }

  @Bean
  public Step deleteOldArticlesStep() {
    return new StepBuilder("deleteOldArticlesStep", jobRepository)
        .tasklet((contribution, chunkContext) -> {
          LocalDateTime cutoff = LocalDate.now().minusDays(3).atStartOfDay();
          List<Article> deleted = articleRepository.deleteAllByPublishDateBefore(cutoff);
          System.out.println("Deleted " + deleted.size() + " old articles.");
          return RepeatStatus.FINISHED;
        }, transactionManager)
        .build();
  }

}
