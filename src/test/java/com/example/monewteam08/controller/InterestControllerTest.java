package com.example.monewteam08.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.request.Interest.InterestUpdateRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.dto.response.interest.InterestWithSubscriptionResponse;
import com.example.monewteam08.dto.response.interest.PageResponse;
import com.example.monewteam08.mapper.InterestMapper;
import com.example.monewteam08.service.impl.InterestServiceImpl;
import com.example.monewteam08.service.impl.SubscriptionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InterestController.class)
@AutoConfigureMockMvc
public class InterestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private InterestServiceImpl interestService;

  @MockitoBean
  private SubscriptionServiceImpl subscriptionService;

  @MockitoBean
  private InterestMapper interestMapper;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("관심사를 등록할 수 있다.")
  void createInterestSuccess() throws Exception {
    //given
    UUID id = UUID.randomUUID();
    InterestRequest request = new InterestRequest("인공지능", List.of("AI", "머신러닝"));
    InterestResponse response = new InterestResponse(id, "인공지능", List.of("AI", "머신러닝"), 0);

    when(interestService.create(any())).thenReturn(response);

    //when && then
    mockMvc.perform(post("/api/interests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Monew-Request-User-Id", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("인공지능"))
        .andExpect(jsonPath("$.keywords[0]").value("AI"))
        .andExpect(jsonPath("$.subscriberCount").value(0));

  }

  @Test
  @DisplayName("검색어, 정렬 기준, 방향, 페이지네이션을 적용해 관심사 목록을 조회")
  void getInterestWithPaginationAndFilters() throws Exception {
    //given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    InterestWithSubscriptionResponse r1 = new InterestWithSubscriptionResponse(id1, "환경보호",
        List.of("ESG"), 100, false);
    InterestWithSubscriptionResponse r2 = new InterestWithSubscriptionResponse(id2, "기후 변화",
        List.of("지구온난화"), 80, false);

    when(interestService.read("환경", "subscriberCount", "DESC", null, null, 10, userId))
        .thenReturn(
            new PageResponse<>(
                List.of(r1, r2),
                null,
                null,
                2,
                10,
                false
            ));

    mockMvc.perform(get("/api/interests")
            .param("keyword", "환경")
            .param("orderBy", "subscriberCount")
            .param("direction", "DESC")
            .param("limit", "10")
            .header("Monew-Request-User-Id", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.content[0].name").value("환경보호"))
        .andExpect(jsonPath("$.content[1].subscriberCount").value(80));

  }

  @Test
  @DisplayName("관심사의 키워드를 수정할 수 있다.")
  void updateInterestKeywordSuccess() throws Exception {
    //given
    UUID id = UUID.randomUUID();
    InterestUpdateRequest request = new InterestUpdateRequest(List.of("GPT", "Depp learning"));
    InterestResponse response = new InterestResponse(id, "인공지능", request.keywords(), 0);

    when(interestService.updateKeywords(eq(id), any())).thenReturn(response);

    //when && then
    mockMvc.perform(patch("/api/interests/{interestId}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Monew-Request-User-Id", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.keywords[0]").value("GPT"))
        .andExpect(jsonPath("$.keywords[1]").value("Depp learning"));
  }

  @Test
  @DisplayName("관심사를 삭제할 수 있다.")
  void deleteInterestSuccess() throws Exception {
    //given
    UUID id = UUID.randomUUID();
    InterestResponse response = new InterestResponse(id, "인공지능", List.of("AI", "Depp learning"), 0);

    when(interestService.delete(id)).thenReturn(response);

    // when & then
    mockMvc.perform(delete("/api/interests/{interestId}", id)
            .header("Monew-Request-User-Id", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("인공지능"));
  }

  @Test
  @DisplayName("관심사 구독을 할 수 있다.")
  void subscribeInterestSuccess() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    mockMvc.perform(post("/api/interests/{interestId}/subscriptions", interestId)
            .header("Monew-Request-User-Id", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("관심사 구독을 취소할 수 있다.")
  void unsubscribeInterestSuccess() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID interestId = UUID.randomUUID();

    mockMvc.perform(delete("/api/interests/{interestId}/subscriptions", interestId)
            .header("Monew-Request-User-Id", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }
}
