package com.example.monewteam08.controller.api;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.request.Interest.InterestUpdateRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.dto.response.interest.InterestWithSubscriptionResponse;
import com.example.monewteam08.dto.response.interest.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "관심사 관리", description = "관심사 관련 API")
public interface InterestControllerDocs {

  @Operation(summary = "관심사 등록", description = "새로운 관심사를 등록합니다.")
  ResponseEntity<CustomApiResponse<InterestResponse>> create(@RequestBody InterestRequest request);

  @Operation(summary = "관심사 목록 조회", description = "조건에 맞는 관심사 목록을 조회합니다.")
  ResponseEntity<CustomApiResponse<PageResponse<InterestWithSubscriptionResponse>>> readAll(
      @Schema(description = "검색어(관심사 이름, 키워드)") @RequestParam(required = false) String keyword,
      @Schema(description = "정렬 속성 이름", allowableValues = {"name",
          "subscriberCount"}) @RequestParam String orderBy,
      @Schema(description = "정렬 방향(ASC, DESC)", allowableValues = {"ASC",
          "DESC"}) @RequestParam(defaultValue = "ASC") String direction,
      @Schema(description = "커서 값") @RequestParam(required = false) UUID cursor,
      @Schema(description = "보조 커서(Cursor) 값") @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,
      @Schema(description = "커서 페이지 크기") @RequestParam(defaultValue = "20") int limit,
      @Schema(description = "요청자 ID") @RequestHeader("Monew-Request-user-Id") UUID userId);

  @Operation(summary = "관심사 정보 수정", description = "관심사의 키워드를 수정합니다.")
  ResponseEntity<CustomApiResponse<InterestResponse>> updateKeywords(
      @Schema(description = "관심사 ID") @PathVariable UUID interestId,
      @RequestBody InterestUpdateRequest request);

  @Operation(summary = "관심사 물리 삭제", description = "관심사를 물리적으로 삭제합니다.")
  ResponseEntity<CustomApiResponse<InterestResponse>> delete(
      @Schema(description = "관심사 ID") @PathVariable UUID interestId);

  @Operation(summary = "관심사 구독", description = "관심사를 구독합니다.")
  ResponseEntity<CustomApiResponse<InterestResponse>> subscribe(
      @Schema(description = "관심사 ID") @PathVariable UUID interestId,
      @Schema(description = "요청자 ID") @RequestHeader("Monew-Request-user-Id") UUID userId);

  @Operation(summary = "관심사 구독 취소", description = "관심사를 구독을 취소합니다.")
  ResponseEntity<CustomApiResponse<InterestResponse>> unsubscribe(
      @Schema(description = "관심사 ID") @PathVariable UUID interestId,
      @Schema(description = "요청자 ID") @RequestHeader("Monew-Request-user-Id") UUID userId);
}
