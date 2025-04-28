package com.example.monewteam08.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import(QuerydslConfig.class)
class ArticleRepositoryTest {

  @Autowired
  private ArticleRepository articleRepository;

  @Test
  void 발행일로_기사_검색_성공() {
    // given
    Article article = new Article("NAVER", "기술 업데이트", "새 스마트폰 출시",
        "http://b.com", LocalDateTime.of(2025, 4, 27, 10, 30), null);
    articleRepository.save(article);

    LocalDateTime startOfDay = LocalDate.of(2025, 4, 27).atStartOfDay();
    LocalDateTime endOfDay = LocalDate.of(2025, 4, 28).atStartOfDay();

    // when
    List<Article> articles = articleRepository.findAllByPublishDateBetween(startOfDay, endOfDay);

    // then
    assertThat(articles.size()).isEqualTo(1);
  }
}