package com.example.monewteam08.controller.api;

import com.example.monewteam08.dto.response.notification.CursorPageResponseNotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "알림 관리", description = "알림 관리 API")
@RequestMapping("/api/notifications")
public interface NotificationControllerDocs {

  @Operation(summary = "알림 목록 조회", description = "알림 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseNotificationDto.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청 (정렬 기준 오류, 페이지네이션 파라미터 오류 등)"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @GetMapping
  ResponseEntity<CursorPageResponseNotificationDto> getNotifications(
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) String after,
      @RequestParam Integer limit,
      @RequestHeader("Monew-Request-User-Id") String requestUserId
  );

  @Operation(summary = "전체 알림 확인", description = "전체 알림을 한번에 확인합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "전체 알림 확인 성공"
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청(입력값 검증 실패)"
      ),
      @ApiResponse(
          responseCode = "404", description = "사용자 정보 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @PatchMapping
  ResponseEntity<Void> ReadAll(
      @RequestHeader("Monew-Request-User-Id") String requestUserId);

  @Operation(summary = "알림 확인", description = "알림을 한번에 확인합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "알림 확인 성공"
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청(입력값 검증 실패)"
      ),
      @ApiResponse(
          responseCode = "404", description = "사용자 정보 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @PatchMapping("/{notificationId}")
  ResponseEntity<Void> Read(
      @PathVariable("notificationId") String notificationId,
      @RequestHeader("Monew-Request-User-Id") String requestUserId);
}
