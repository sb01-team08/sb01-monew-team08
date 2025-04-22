package com.example.monewteam08.controller;

import com.example.monewteam08.common.CustomApiResponse;
import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.mapper.InterestMapper;
import com.example.monewteam08.service.Interface.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {

  private final InterestService interestService;
  private final InterestMapper interestMapper;

  @PostMapping
  public ResponseEntity<CustomApiResponse<InterestResponse>> create(
      @RequestBody InterestRequest request) {
    Interest interest = interestService.create(request.name(), request.keywords());
    InterestResponse response = interestMapper.toResponse(interest);
    return ResponseEntity.ok(CustomApiResponse.ok(response));
  }

}
