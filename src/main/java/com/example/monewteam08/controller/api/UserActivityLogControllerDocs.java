package com.example.monewteam08.controller.api;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.dto.response.useractivitylog.UserActivityLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사용자 활동 내역 관리", description = "사용자 활동 내역 관련 API")
public interface UserActivityLogControllerDocs {

  @Operation(summary = "사용자 활동 내역 조회", description = "사용자 ID로 활동 내역을 조회합니다.")
  CustomApiResponse<UserActivityLogResponse> getUserActivityLog(@PathVariable UUID userId);
}
