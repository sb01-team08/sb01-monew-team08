package com.example.monewteam08.exception.notification;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;

public class NotificationDeleteJobFailed extends MonewException {

  public NotificationDeleteJobFailed() {
    super(ErrorCode.NOTIFICATION_DELETE_JOB_FAILED);
  }
}
