package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    UUID softDeletedId = UUID.randomUUID();
    UUID lostId = UUID.randomUUID();
    LocalDateTime from = LocalDateTime.now().minusDays(1);
    LocalDateTime to = from.plusDays(1);
    Path dummyPath = Path.of("/tmp/articles_" + from.toLocalDate() + ".csv");

    Article softDeleted = Article.withId(softDeletedId, "NAVER", "제목", "요약", "http://test1.com",
        from.plusHours(8), null);
    softDeleted.softDelete();
    Article activated = Article.withId(softDeletedId, "NAVER", "제목", "요약", "http://test-active.com",
        from.plusHours(8), null);

    Article lost = Article.withId(lostId, "NAVER", "제목", "요약", "http://test2.com",
        from.plusHours(8), null);

    given(s3Service.download(from.toLocalDate())).willReturn(dummyPath);
    given(csvService.importArticlesFromCsv(dummyPath)).willReturn(List.of(lost, softDeleted));

    given(articleRepository.findAllById(List.of(lostId, softDeletedId)))
        .willReturn(List.of(softDeleted));

    given(articleRepository.saveAll(List.of(lost))).willReturn(List.of(lost));
    given(articleRepository.saveAll(List.of(softDeleted))).willReturn(List.of(activated));

    // when
    ArticleRestoreResultDto result = articleBackupService.restore(from, to);

    // then
    assertThat(result.restoredArticleIds()).containsExactly(lostId);
    assertThat(result.restoredArticleCount()).isEqualTo(1);

    verify(articleRepository).saveAll(List.of(lost));
    verify(articleRepository).saveAll(List.of(softDeleted));

  }


}
