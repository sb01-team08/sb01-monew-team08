package com.example.monewteam08.batch;

import io.micrometer.core.instrument.MeterRegistry;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchJobMetricsListener implements JobExecutionListener {

  private final MeterRegistry meterRegistry;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    meterRegistry.counter("batch.job.start", "job", jobExecution.getJobInstance().getJobName())
        .increment();
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    String jobName = jobExecution.getJobInstance().getJobName();
    BatchStatus status = jobExecution.getStatus();

    meterRegistry.counter("batch.job.completed", "job", jobName, "status", status.name())
        .increment();

    long duration = Duration.between(jobExecution.getStartTime().toInstant(ZoneOffset.UTC),
            jobExecution.getEndTime().toInstant(ZoneOffset.UTC))
        .toMillis();
    meterRegistry.timer("batch.job.duration", "job", jobName)
        .record(duration, TimeUnit.MILLISECONDS);
  }

}
