package com.example.monewteam08.controller;

import com.example.monewteam08.dto.response.notification.CursorPageResponseNotificationDto;
import com.example.monewteam08.service.Interface.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<CursorPageResponseNotificationDto> getNotifications(
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) String after,
      @RequestParam Integer limit,
      @RequestHeader("Monew-Request-User-Id") String requestUserId
  ) {
    CursorPageResponseNotificationDto result = notificationService.getUnreadNotifications(
        requestUserId, cursor, after, limit);
    return ResponseEntity.ok(result);
  }

  @PatchMapping
  public ResponseEntity<Void> ReadAll(
      @RequestHeader("Monew-Request-User-Id") String requestUserId) {
    notificationService.confirmAllNotifications(requestUserId);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{notificationId}")
  public ResponseEntity<Void> Read(
      @PathVariable("notificationId") String notificationId,
      @RequestHeader("Monew-Request-User-Id") String requestUserId) {
    notificationService.confirmNotification(requestUserId, notificationId);
    return ResponseEntity.ok().build();
  }
}
