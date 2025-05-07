package com.example.monewteam08.batch;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import com.example.monewteam08.service.Interface.ArticleService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FetchAndSaveArticlesJobConfig {

  private final UserRepository userRepository;
  private final ArticleService articleService;
  private final ArticleFetchService articleFetchService;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  private List<Article> allArticles;

  @Bean
  public Job fetchAndFilterArticlesJob(BatchJobMetricsListener batchJobMetricsListener) {
    return new JobBuilder("fetchAndFilterArticlesJob", jobRepository)
        .start(loadArticlesStep())
        .next(filterAndNotifyStep())
        .listener(batchJobMetricsListener)
        .build();
  }

  @Bean
  public Step loadArticlesStep() {
    return new StepBuilder("loadArticlesStep", jobRepository)
        .tasklet((contribution, context) -> {
          List<UUID> userIds = userRepository.findAll().stream()
              .map(User::getId)
              .toList();

          context.getStepContext().getStepExecution()
              .getJobExecution().getExecutionContext()
              .put("userIds", userIds);

          allArticles = articleFetchService.fetchAllArticles();
          return RepeatStatus.FINISHED;
        }, transactionManager)
        .build();
  }

  @Bean
  public Step filterAndNotifyStep() {
    return new StepBuilder("filterAndNotifyStep", jobRepository)
        .<UUID, UUID>chunk(10, transactionManager)
        .reader(userIdReader(null))
        .writer(userIds -> {
          for (UUID userId : userIds) {
            articleService.filterAndSave(userId, allArticles);
          }
        })
        .build();
  }

  @Bean
  @StepScope
  public ItemReader<UUID> userIdReader(
      @Value("#{jobExecutionContext['userIds']}") List<UUID> userIds) {
    return new IteratorItemReader<>(userIds);
  }

}
