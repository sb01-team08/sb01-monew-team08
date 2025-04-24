package com.example.monewteam08.controller;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.controller.api.InterestControllerDocs;
import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.request.Interest.InterestUpdateRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.dto.response.interest.InterestWithSubscriptionResponse;
import com.example.monewteam08.dto.response.interest.PageResponse;
import com.example.monewteam08.mapper.InterestMapper;
import com.example.monewteam08.service.Interface.InterestService;
import com.example.monewteam08.service.Interface.SubscriptionService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController implements InterestControllerDocs {

  private final InterestService interestService;
  private final InterestMapper interestMapper;
  private final SubscriptionService subscriptionService;

  @PostMapping
  public ResponseEntity<CustomApiResponse<InterestResponse>> create(
      @RequestBody InterestRequest request
  ) {
    InterestResponse response = interestService.create(request);
    return ResponseEntity.ok(CustomApiResponse.ok(response));
  }

  @GetMapping
  public ResponseEntity<CustomApiResponse<PageResponse<InterestWithSubscriptionResponse>>> readAll(
      @RequestParam(required = false) String keyword,
      @RequestParam String orderBy,
      @RequestParam(defaultValue = "ASC") String direction,
      @RequestParam(required = false) UUID cursor,
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,
      @RequestParam(defaultValue = "20") int limit,
      @RequestHeader("Monew-Request-User-Id") UUID userId
  ) {
    PageResponse<InterestWithSubscriptionResponse> result = interestService.read(
        keyword, orderBy, direction, cursor, after, limit, userId
    );

    return ResponseEntity.ok(CustomApiResponse.ok(result));
  }

  @PatchMapping("/{interestId}")
  public ResponseEntity<CustomApiResponse<InterestResponse>> updateKeywords(
      @PathVariable UUID interestId,
      @RequestBody InterestUpdateRequest request
  ) {
    InterestResponse response = interestService.updateKeywords(interestId, request);
    return ResponseEntity.ok(CustomApiResponse.ok(response));
  }

  @DeleteMapping("/{interestId}")
  public ResponseEntity<CustomApiResponse<InterestResponse>> delete(
      @PathVariable UUID interestId
//      @RequestHeader("Monew-Request_user=Id") UUID userId
  ) {
    InterestResponse deleted = interestService.delete(interestId);
    return ResponseEntity.ok(CustomApiResponse.ok(deleted));
  }

  @PostMapping("/{interestId}/subscriptions")
  public ResponseEntity<CustomApiResponse<InterestResponse>> subscribe(
      @PathVariable UUID interestId,
      @RequestHeader("Monew-Request-User-Id") UUID userId
  ) {
    subscriptionService.subscribe(interestId, userId);
    return ResponseEntity.ok(CustomApiResponse.ok(null));
  }

  @DeleteMapping("/{interestId}/subscriptions")
  public ResponseEntity<CustomApiResponse<InterestResponse>> unsubscribe(
      @PathVariable UUID interestId,
      @RequestHeader("Monew-Request-User-Id") UUID userId
  ) {
    subscriptionService.unsubscribe(interestId, userId);
    return ResponseEntity.ok(CustomApiResponse.ok(null));
  }

}

