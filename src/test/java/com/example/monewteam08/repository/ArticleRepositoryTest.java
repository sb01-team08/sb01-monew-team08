package com.example.monewteam08.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import java.time.LocalDate;
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
    LocalDate publishDate = LocalDate.of(2025, 4, 28);

    // when
    List<Article> articles = articleRepository.findByPublishDate(publishDate);

    // then
    assertThat(articles).isNotEmpty();
  }
}