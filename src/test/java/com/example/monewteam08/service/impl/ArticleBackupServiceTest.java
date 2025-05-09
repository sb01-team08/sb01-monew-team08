package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.example.monewteam08.dto.response.article.ArticleRestoreResultDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.service.Interface.CsvService;
import com.example.monewteam08.service.Interface.S3Service;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
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
    LocalDateTime from = LocalDateTime.of(2025, 4, 29, 0, 0);
    LocalDateTime to = from.plusDays(1);
    UUID lostId = UUID.randomUUID();
    UUID softDeletedId = UUID.randomUUID();

    Article lostArticle = Article.withId(lostId, "NAVER", "제목", "요약", "http://lost.com",
        from.plusHours(1), null);
    Article softDeletedArticle = Article.withId(softDeletedId, "NAVER", "제목", "요약",
        "http://deleted.com", from.plusHours(2), null);
    softDeletedArticle.softDelete();

    Path dummyPath = Path.of("/tmp/articles_" + from.toLocalDate() + ".csv");

    given(s3Service.download(from.toLocalDate())).willReturn(dummyPath);
    given(s3Service.download(to.toLocalDate())).willReturn(dummyPath);
    given(csvService.importArticlesFromCsv(dummyPath))
        .willReturn(List.of(lostArticle, softDeletedArticle))
        .willReturn(List.of());
    given(articleRepository.findAllById(any())).willReturn(List.of(softDeletedArticle));

    given(articleRepository.saveAll(anyList())).willAnswer(invocation -> {
      List<Article> articles = invocation.getArgument(0);
      for (Article a : articles) {
        if (a.getId() == null) {
          setField(a, "id", lostId);
        }
      }
      return articles;
    });
    // when
    ArticleRestoreResultDto result = articleBackupService.restore(from, to);

    // then
    assertThat(result.restoredArticleCount()).isEqualTo(1);
    assertThat(result.restoredArticleIds()).containsExactly(lostId);

    verify(articleRepository, times(1)).saveAll(argThat(iterable ->
        StreamSupport.stream(iterable.spliterator(), false)
            .anyMatch(a -> a.getSourceUrl().equals("http://lost.com"))
    ));

    verify(articleRepository, times(1)).saveAll(argThat(iterable ->
        StreamSupport.stream(iterable.spliterator(), false)
            .anyMatch(a -> a.getSourceUrl().equals("http://deleted.com"))
    ));
  }


}
