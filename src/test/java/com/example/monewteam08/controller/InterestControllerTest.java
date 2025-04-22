package com.example.monewteam08.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.mapper.InterestMapper;
import com.example.monewteam08.service.impl.InterestServiceImpl;
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
  private InterestMapper interestMapper;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  @DisplayName("관심사를 등록할 수 있다.")
  void createInterestSuccess() throws Exception {
    //given
    UUID id = UUID.randomUUID();
    InterestRequest request = new InterestRequest("인공지능", List.of("AI", "머신러닝"));
    Interest interest = new Interest(id, request.name(), request.keywords(), 0);
    InterestResponse response = new InterestResponse(id, "인공지능", List.of("AI", "머신러닝"), 0);

    when(interestService.create(any(), any())).thenReturn(interest);
    when(interestMapper.toResponse(any())).thenReturn(response);

    //when && then
    mockMvc.perform(post("/api/interests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(id.toString()))
        .andExpect(jsonPath("$.data.name").value("인공지능"))
        .andExpect(jsonPath("$.data.keywords[0]").value("AI"))
        .andExpect(jsonPath("$.data.subscriberCount").value(0));

  }

}
