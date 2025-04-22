package com.example.monewteam08.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.monewteam08.service.Interface.ArticleService;
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

  @Test
  void 기사_논리_삭제_API_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/articles/{id}", articleId))
        .andExpect(status().isNoContent());

    verify(articleService).softDelete(articleId);
  }

  @Test
  void 기사_물리_삭제_API_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/articles/{id}/hard", articleId))
        .andExpect(status().isNoContent());

    verify(articleService).hardDelete(articleId);
  }

}
