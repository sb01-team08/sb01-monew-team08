package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.request.Interest.InterestUpdateRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.dto.response.interest.InterestWithSubscriptionResponse;
import com.example.monewteam08.dto.response.interest.PageResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface InterestService {

  //관심사 생성
  public InterestResponse create(InterestRequest request);

  //관심사 조회
  public List<InterestResponse> read(String query, String sortBy);

  public PageResponse<InterestWithSubscriptionResponse> read(
      String keyword,
      String orderBy,
      String direction,
      UUID cursor,
      LocalDateTime after,
      int limit,
      UUID userId
  );

  //관심사 키워스 수정
  public InterestResponse updateKeywords(UUID id, InterestUpdateRequest newKeywords);

  //관심사 삭제
  public InterestResponse delete(UUID id);

}
