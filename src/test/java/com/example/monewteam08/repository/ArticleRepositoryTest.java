package com.example.monewteam08.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import(QuerydslConfig.class)
class ArticleRepositoryTest {

  @Autowired
  private ArticleRepository articleRepository;

  @Test
  void 기사_전체_조회() {
    // given
    Article article1 = new Article("NAVER", "제목1", "내용1", "url1", LocalDateTime.now());
    Article article2 = new Article("NAVER", "제목2", "내용2", "url2", LocalDateTime.now());
    Article article3 = new Article("NAVER", "제목3", "내용3", "url3", LocalDateTime.now());

    articleRepository.save(article1);
    articleRepository.save(article2);
    articleRepository.save(article3);

    Specification<Article> spec = (root, query, cb) -> cb.isTrue(root.get("isActive"));
    Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "publishedAt");

    // when
    Page<Article> result = articleRepository.findAll(spec, pageable);

    // then
    assertThat(result.getTotalElements()).isEqualTo(3);
  }
}