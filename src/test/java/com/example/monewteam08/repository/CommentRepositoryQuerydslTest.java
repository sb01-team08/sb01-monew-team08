package com.example.monewteam08.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class CommentRepositoryQuerydslTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ArticleRepository articleRepository;

  private UUID articleId;

  @BeforeEach
  void setUp() {
    Article article = new Article("네이버", "제목", "요약", "123", LocalDateTime.now(), null);
    articleRepository.save(article);
    articleId = article.getId();
    LocalDateTime baseTime = LocalDateTime.of(2023, 1, 1, 0, 0);
    for (int i = 0; i < 100; i++) {
      Comment comment = new Comment(articleId, UUID.randomUUID(), "댓글 " + i);
      ReflectionTestUtils.setField(comment, "createdAt", baseTime.plusSeconds(i * 10));
      ReflectionTestUtils.setField(comment, "likeCount", i);
      commentRepository.save(comment);
    }
  }

  @Test
  void createdAt_ASC_페이지네이션_테스트() {
    List<Comment> firstPageRaw = commentRepository.findAllByCursor(
        articleId, "createdAt", "ASC", null, null, 10
    );

    List<Comment> firstPage = firstPageRaw.size() > 10
        ? firstPageRaw.subList(0, 10)
        : firstPageRaw;

    assertThat(firstPage).hasSize(10);

    LocalDateTime cursor = firstPage.get(9).getCreatedAt();
    UUID afterId = firstPage.get(9).getId();

    System.out.println("✅ [1페이지] firstPage.size() = " + firstPage.size());
    firstPage.forEach(c ->
        System.out.println("📌 댓글: " + c.getContent() + ", createdAt: " + c.getCreatedAt())
    );

    List<Comment> secondPageRaw = commentRepository.findAllByCursor(
        articleId, "createdAt", "ASC", cursor.toString(), afterId.toString(), 11
    );

    List<Comment> secondPage = secondPageRaw.size() > 10
        ? secondPageRaw.subList(0, 10)
        : secondPageRaw;

    assertThat(secondPage).hasSize(10);
    assertThat(secondPage.get(0).getContent()).isEqualTo("댓글 10");
  }

  @Test
  void createdAt_DESC_페이지네이션_테스트() {
    List<Comment> firstPageRaw = commentRepository.findAllByCursor(
        articleId, "createdAt", "DESC", null, null, 10
    );

    List<Comment> firstPage = firstPageRaw.size() > 10
        ? firstPageRaw.subList(0, 10)
        : firstPageRaw;

    assertThat(firstPage).hasSize(10);

    LocalDateTime cursor = firstPage.get(9).getCreatedAt();
    UUID afterId = firstPage.get(9).getId();

    List<Comment> secondPageRaw = commentRepository.findAllByCursor(
        articleId, "createdAt", "DESC", cursor.toString(), afterId.toString(), 10
    );

    List<Comment> secondPage = secondPageRaw.size() > 10
        ? secondPageRaw.subList(0, 10)
        : secondPageRaw;

    assertThat(secondPage).hasSize(10);
    assertThat(secondPage.get(0).getContent()).isEqualTo("댓글 89");
  }

  @Test
  void likeCount_ASC_페이지네이션_테스트() {
    List<Comment> firstPageRaw = commentRepository.findAllByCursor(
        articleId, "likeCount", "ASC", "0", null, 10
    );

    List<Comment> firstPage = firstPageRaw.size() > 10
        ? firstPageRaw.subList(0, 10)
        : firstPageRaw;

    assertThat(firstPage).hasSize(10);

    int lastLikeCount = firstPage.get(9).getLikeCount();
    LocalDateTime lastCreatedAt = firstPage.get(9).getCreatedAt();
    UUID lastId = firstPage.get(9).getId();

    List<Comment> secondPageRaw = commentRepository.findAllByCursor(
        articleId, "likeCount", "ASC",
        String.valueOf(lastLikeCount), lastId.toString(), 10
    );

    List<Comment> secondPage = secondPageRaw.size() > 10
        ? secondPageRaw.subList(0, 10)
        : secondPageRaw;

    assertThat(secondPage).hasSize(10);
    assertThat(secondPage.get(0).getLikeCount()).isGreaterThanOrEqualTo(lastLikeCount);
  }

  @Test
  void likeCount_DESC_페이지네이션_테스트() {
    List<Comment> firstPageRaw = commentRepository.findAllByCursor(
        articleId, "likeCount", "DESC", "99", null, 10
    );

    List<Comment> firstPage = firstPageRaw.size() > 10
        ? firstPageRaw.subList(0, 10)
        : firstPageRaw;

    assertThat(firstPage).hasSize(10);

    int lastLikeCount = firstPage.get(9).getLikeCount();
    LocalDateTime lastCreatedAt = firstPage.get(9).getCreatedAt();
    UUID lastId = firstPage.get(9).getId();

    List<Comment> secondPageRaw = commentRepository.findAllByCursor(
        articleId, "likeCount", "DESC",
        String.valueOf(lastLikeCount), lastId.toString(), 10
    );

    List<Comment> secondPage = secondPageRaw.size() > 10
        ? secondPageRaw.subList(0, 10)
        : secondPageRaw;

    assertThat(secondPage).hasSize(10);
    assertThat(secondPage.get(0).getLikeCount()).isLessThanOrEqualTo(lastLikeCount);
  }
}
