package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.nodtification.NotificationDto;
import com.example.monewteam08.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

  public NotificationDto toDto(Notification notification) {
    if (notification == null) {
      return null;
    }
    return NotificationDto.builder()
        .id(notification.getId().toString())
        .createdAt(notification.getCreatedAt())
        .updatedAt(notification.getUpdatedAt())
        .confirmed(false)
        .userId(notification.getUserId().toString())
        .content(notification.getContent())
        .resourceType(notification.getResource_type().toString())
        .resourceId(notification.getResourceId().toString())
        .build();
  }
}
