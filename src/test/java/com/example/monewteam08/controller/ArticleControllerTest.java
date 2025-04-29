package com.example.monewteam08.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.monewteam08.dto.response.article.ArticleRestoreResultDto;
import com.example.monewteam08.service.Interface.ArticleBackupService;
import com.example.monewteam08.service.Interface.ArticleService;
import com.example.monewteam08.service.Interface.ArticleViewService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ArticleService articleService;

  @MockitoBean
  private ArticleViewService articleViewService;

  @MockitoBean
  private ArticleBackupService articleBackupService;

  @Test
  void 기사_논리_삭제_API_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();

    willDoNothing().given(articleService).softDelete(articleId);

    // when & then
    mockMvc.perform(
            delete("/api/articles/{id}", articleId).header("Monew-Request-User-Id", UUID.randomUUID()))
        .andExpect(status().isNoContent());

    verify(articleService).softDelete(articleId);
  }

  @Test
  void 기사_물리_삭제_API_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/articles/{id}/hard", articleId).header("Monew-Request-User-Id",
        UUID.randomUUID())).andExpect(status().isNoContent());

    verify(articleService).hardDelete(articleId);
  }

  @Test
  void 기사_목록_조회_API_성공() throws Exception {
    // given
    String keyword = "test";
    UUID interestId = UUID.randomUUID();
    String orderBy = "createdAt";
    String direction = "desc";
    String cursor = null;
    int limit = 10;

    // when & then
    mockMvc.perform(get("/api/articles").header("Monew-Request-User-Id", UUID.randomUUID())
        .param("keyword", keyword).param("interestId", interestId.toString())
        .param("orderBy", orderBy).param("direction", direction).param("cursor", cursor)
        .param("limit", String.valueOf(limit))).andExpect(status().isOk());
  }

  @Test
  void 기사_뷰_등록_API_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    // when & then
    mockMvc.perform(
        post("/api/articles/{id}/article-views", articleId).param("userId", userId.toString())
            .header("Monew-Request-User-Id", userId.toString())).andExpect(status().isOk());

    verify(articleViewService).save(any(), any());
  }

  @Test
  void 기사_복구_API_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();
    LocalDateTime from = LocalDateTime.of(2024, 4, 28, 0, 0);
    LocalDateTime to = LocalDateTime.of(2024, 4, 29, 23, 59);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    ArticleRestoreResultDto resultDto = new ArticleRestoreResultDto(LocalDateTime.now(),
        List.of(articleId), 1);

    given(articleBackupService.restore(any(LocalDateTime.class), any(LocalDateTime.class)))
        .willReturn(resultDto);

    // when & then
    mockMvc.perform(get("/api/articles/restore")
            .param("from", from.toString())
            .param("to", to.toString()))
        .andExpect(status().isOk());

    verify(articleBackupService).restore(from, to);
  }

}
