package com.example.monewteam08.util;

import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

// 아이디 자동으로 mongodb에 저장
@Component
public class IdAutoGeneratorListener {

  private final SequenceGenerator sequenceGenerator;

  public IdAutoGeneratorListener(SequenceGenerator sequenceGenerator) {
    this.sequenceGenerator = sequenceGenerator;
  }

  @EventListener
  public void onBeforeConvert(BeforeConvertEvent<?> event) {
    Object source = event.getSource();

    if (source instanceof Notification notification && notification.getId() == null) {
      notification.setId(sequenceGenerator.getNextSequence("notifications"));
    }

    if (source instanceof UserActivityLog log && log.getId() == null) {
      log.setId(sequenceGenerator.getNextSequence("user_activity_logs"));
    }
  }
}

