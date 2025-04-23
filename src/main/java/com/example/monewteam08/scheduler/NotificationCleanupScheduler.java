package com.example.monewteam08.scheduler;

import com.example.monewteam08.service.Interface.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {

  private final NotificationService notificationService;

  @Scheduled(cron = "0 0 */6 * * *" )
  public void deleteNotifications() {
    notificationService.deleteNotification();
  }
}
