package com.example.monewteam08.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.entity.Interest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InterestMapperTest {

  private final InterestMapper interestMapper = new InterestMapper();

  @Test
  @DisplayName("관심사 객체를 받아 InterestResponse 반환")
  public void toInterestResponse() {
    //given
    Interest interest = new Interest(UUID.randomUUID(), "기후변화", List.of("지구온난화"), 0);

    //when
    InterestResponse interestResponse = interestMapper.toResponse(interest);

    // then
    assertThat(interestResponse).isNotNull();
    assertThat(interestResponse).isInstanceOf(InterestResponse.class);
    assertThat(interestResponse.name()).isEqualTo(interest.getName());
    assertThat(interestResponse.keywords()).isEqualTo(interest.getKeywords());
  }

  @Test
  @DisplayName("InterestResponse 를 받아 관심사 객체를 반환")
  public void toInterestRequest() {
    //given
    InterestRequest request = new InterestRequest("인공지능", List.of("AI", "머신러닝"));
    // when
    Interest interest = interestMapper.toEntity(request);

    assertThat(interest.getName()).isEqualTo(request.name());
    assertThat(interest.getKeywords()).isEqualTo(request.keywords());
    assertThat(interest.getSubscriberCount()).isEqualTo(0);

  }
}
