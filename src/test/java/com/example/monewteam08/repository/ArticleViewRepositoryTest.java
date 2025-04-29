package com.example.monewteam08.repository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.ArticleView;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import(QuerydslConfig.class)
class ArticleViewRepositoryTest {

  @Autowired
  private ArticleViewRepository articleViewRepository;

  @Test
  void 유저_id와_기사_id로_조회() {
    // given
    UUID userId = UUID.randomUUID();
    UUID articleId = UUID.randomUUID();
    ArticleView articleView = new ArticleView(userId, articleId);

    articleViewRepository.save(articleView);

    // when
    Optional<ArticleView> result = articleViewRepository.findByUserIdAndArticleId(userId,
        articleId);

    // then
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(articleView);
  }
}