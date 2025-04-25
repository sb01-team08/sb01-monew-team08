package com.example.monewteam08.controller;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.controller.api.UserActivityLogControllerDocs;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import com.example.monewteam08.service.Interface.UserActivityLogService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-activities")
@RequiredArgsConstructor
public class UserActivityLogController implements UserActivityLogControllerDocs {

  private final UserActivityLogService userActivityLogService;

  @GetMapping("/{userId}")
  public CustomApiResponse<UserActivityLogResponse> getUserActivityLog(
      @PathVariable UUID userId
  ) {
    return CustomApiResponse.ok(userActivityLogService.getUserActivityLog(userId));
  }

}
