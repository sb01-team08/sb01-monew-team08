package com.example.monewteam08.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.service.Interface.CsvService;
import com.example.monewteam08.service.Interface.S3Service;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ArticleBackupServiceTest {

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private CsvService csvService;

  @Mock
  private S3Service s3Service;

  @InjectMocks
  private ArticleBackupServiceImpl articleBackupService;

  @Test
  void 백업_성공() {
    // given
    LocalDate yesterday = LocalDate.now().minusDays(1);
    List<Article> articles = List.of(
        Article.withId(UUID.randomUUID(), "NAVER", "제목", "요약", "http://test.com",
            yesterday.minusDays(1).atTime(8, 0), null)
    );

    when(articleRepository.findAllByPublishDateBetween(any(), any()))
        .thenReturn(articles);
    when(csvService.exportArticlesToCsv(any(Path.class), eq(articles)))
        .thenReturn(Path.of("/tmp/articles_" + yesterday + ".csv"));

    // when
    articleBackupService.backup();

    // then
    verify(s3Service).upload(any(Path.class), eq(yesterday));
  }

  @Test
  void 복구_성공() {
    // given
    LocalDateTime from = LocalDateTime.now().minusDays(1);
    LocalDateTime to = from.plusDays(1);
    Path dummyPath = Path.of("/tmp/articles_" + from.toLocalDate() + ".csv");

    List<Article> articles = List.of(
        Article.withId(UUID.randomUUID(), "NAVER", "제목", "요약", "http://test.com",
            from.plusHours(8), null)
    );

    when(s3Service.download(from.toLocalDate())).thenReturn(dummyPath);
    when(csvService.importArticlesFromCsv(dummyPath)).thenReturn(articles);

    // when
    articleBackupService.restore(from, to);

    // then
    verify(articleRepository).saveAll(articles);
  }
}
