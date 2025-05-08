package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.entity.User;
import java.util.UUID;

public interface UserActivityMService {

  // UserActivity
  void createUserActivity(User user);

  UserActivityLogResponse getUserActivityLog(UUID userId);
}
