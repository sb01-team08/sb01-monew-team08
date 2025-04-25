package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import java.util.UUID;

public interface UserActivityLogService {

  // 조회
  UserActivityLogResponse getUserActivityLog(UUID userId);

}
