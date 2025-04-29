package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.service.Interface.CsvService;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class CsvServiceTest {

  @Test
  void CSV로_내보내기_성공() {
    // given
    CsvService csvService = new CsvServiceImpl();
    LocalDate yesterday = LocalDate.now().minusDays(1);
    UUID articleId = UUID.randomUUID();
    Article article = new Article("NAVER", "title", "summary", "http://example.com",
        LocalDateTime.now(), null);
    List<Article> articles = List.of(article);
    ReflectionTestUtils.setField(article, "id", articleId);

    String fileName = "articles_" + yesterday + ".csv";
    Path tmpPath = Path.of(System.getProperty("java.io.tmpdir"), fileName);

    // when
    Path path = csvService.exportArticlesToCsv(tmpPath, articles);

    //then
    assertThat(path).isNotNull();
    assertThat(Files.exists(path)).isTrue();
    assertThat(path.toString()).contains("articles_" + yesterday + ".csv");
  }

  @Test
  void CSV로부터_복구하기_성공() throws Exception {
    // given
    UUID articleId = UUID.randomUUID();
    CsvService csvService = new CsvServiceImpl();
    LocalDate yesterday = LocalDate.now().minusDays(1);
    Article article = new Article("NAVER", "title", "summary", "http://example.com",
        LocalDateTime.now(), null);
    Path tempFile = Files.createTempFile("articles_" + yesterday, ".csv");

    try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
      writer.write("id,source,title,summary,sourceUrl,publishDate,interestId\n"); // 헤더
      writer.write(
          articleId + ",NAVER,title,summary,http://example.com,2025-04-25T10:00:00,null\n");
    }

    // when
    List<Article> result = csvService.importArticlesFromCsv(tempFile);

    // then
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(1);

    Files.deleteIfExists(tempFile);
  }
}
