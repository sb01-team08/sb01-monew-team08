package com.example.monewteam08.scheduler;

import com.example.monewteam08.exception.notification.NotificationDeleteJobFailed;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {

  private final JobLauncher jobLauncher;
  private final Job deleteNotificationsJob;

  @Scheduled(cron = "0 0 */6 * * *")
  public void deleteNotifications() {
    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addLong("timestamp", System.currentTimeMillis())
          .toJobParameters();
      jobLauncher.run(deleteNotificationsJob, jobParameters);
    } catch (Exception e) {
      throw new NotificationDeleteJobFailed();
    }
  }
}
