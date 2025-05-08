package com.example.monewteam08.batch;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
public class ArticleBatchJobTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private Job fetchAndSaveArticlesJob;

  @Autowired
  private Job backupArticlesJob;

  @Autowired
  private Job deleteOldArticlesJob;

  @Test
  void fetchAndFilterArticlesJob_정상_실행() throws Exception {
    // given
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    // when
    JobExecution execution = jobLauncherTestUtils.getJobLauncher()
        .run(fetchAndSaveArticlesJob, params);

    // then
    assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
  }

  @Test
  void backupArticlesJob_정상_실행() throws Exception {
    // given
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    // when
    JobExecution execution = jobLauncherTestUtils.getJobLauncher()
        .run(backupArticlesJob, params);

    // then
    assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
  }

  @Test
  void deleteOldArticlesJob_정상_실행() throws Exception {
    // given
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    // when
    JobExecution execution = jobLauncherTestUtils.getJobLauncher()
        .run(deleteOldArticlesJob, params);

    // then
    assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
  }
}
