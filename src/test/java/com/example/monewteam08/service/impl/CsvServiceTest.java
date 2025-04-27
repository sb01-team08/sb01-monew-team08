package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.service.Interface.CsvService;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CsvServiceTest {

  @Mock
  private CsvService csvService;

  @Test
  void CSV로_내보내기_성공() {
    // given
    UUID articleId = UUID.randomUUID();
    Article article = new Article("NAVER", "title", "summary", "http://example.com",
        LocalDateTime.now(), null);
    List<Article> articles = List.of(article);
    when(csvService.exportArticlesToCsv(LocalDate.now(), articles)).thenReturn(
        Path.of("articles_2025-04-25.csv"));

    // when
    Path path = csvService.exportArticlesToCsv(LocalDate.now(), articles);

    //then
    assertThat(path).isNotNull();
    assertThat(path.toString()).contains("articles_2025-04-25.csv");
  }
}
