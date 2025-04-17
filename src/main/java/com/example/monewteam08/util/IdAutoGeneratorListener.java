package com.example.monewteam08.util;

import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

// 아이디 자동으로 mongodb에 저장
@Component
public class IdAutoGeneratorListener {

  private final SequenceGenerator sequenceGenerator;

  /****
   * Constructs an IdAutoGeneratorListener with the specified sequence generator.
   *
   * @param sequenceGenerator the sequence generator used to assign unique IDs to entities before saving
   */
  public IdAutoGeneratorListener(SequenceGenerator sequenceGenerator) {
    this.sequenceGenerator = sequenceGenerator;
  }

  /**
   * Assigns a sequential ID to Notification or UserActivityLog entities before they are converted and saved to MongoDB.
   *
   * If the entity's ID is null, a new unique ID is generated and set using the appropriate sequence for its collection.
   *
   * @param event the event triggered before an entity is converted for MongoDB persistence
   */
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

