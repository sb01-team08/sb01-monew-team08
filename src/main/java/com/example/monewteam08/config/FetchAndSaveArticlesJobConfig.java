package com.example.monewteam08.config;

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
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.repeat.RepeatStatus;
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

  private List<UUID> allUserIds;
  private List<Article> allArticles;

  @Bean
  public Job fetchAndFilterArticlesJob() {
    return new JobBuilder("fetchAndFilterArticlesJob", jobRepository)
        .start(loadArticlesStep())
        .next(filterAndNotifyStep())
        .build();
  }

  @Bean
  public Step loadArticlesStep() {
    return new StepBuilder("loadArticlesStep", jobRepository)
        .tasklet((contribution, context) -> {
          allUserIds = userRepository.findAll().stream()
              .map(User::getId)
              .toList();
          allArticles = articleFetchService.fetchAllArticles();
          return RepeatStatus.FINISHED;
        }, transactionManager)
        .build();
  }

  @Bean
  public Step filterAndNotifyStep() {
    return new StepBuilder("filterAndNotifyStep", jobRepository)
        .<UUID, UUID>chunk(10, transactionManager)
        .reader(new IteratorItemReader<>(allUserIds))
        .writer(userIds -> {
          for (UUID userId : userIds) {
            articleService.filterAndSave(userId, allArticles);
          }
        })
        .build();
  }

}
