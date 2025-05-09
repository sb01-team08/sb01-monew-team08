package com.example.monewteam08.mapper;


import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.entity.Interest;
import org.springframework.stereotype.Component;

@Component
public class InterestMapper {

  public Interest toEntity(InterestRequest request) {
    return new Interest(request.name(), request.keywords());
  }

  public InterestResponse toResponse(Interest interest) {
    return new InterestResponse(
        interest.getId(),
        interest.getName(),
        interest.getKeywords(),
        interest.getSubscriberCount()
    );
  }

}
