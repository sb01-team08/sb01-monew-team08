package com.example.monewteam08.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class CommentRepositoryTest {

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
  }

  @Test
  void savedAndFindById() {
    Comment comment = new Comment(
        articleId,
        UUID.randomUUID(),
        "테스트"
    );

    Comment save = commentRepository.save(comment);
    Optional<Comment> result = commentRepository.findById(save.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getContent()).isEqualTo("테스트");
  }

  @Test
  void 존재하지_않는_댓글_조회시_예외발생() {
    UUID notExistsId = UUID.randomUUID();

    assertThatCode(() -> {
      commentRepository.findById(notExistsId)
          .orElseThrow(CommentNotFoundException::new);
    }).isInstanceOf(CommentNotFoundException.class)
        .hasMessageContaining("댓글을 찾을 수 없습니다");
  }

  @Test
  void existsById() {
    Comment comment = new Comment(
        articleId,
        UUID.randomUUID(),
        "테스트"
    );
    UUID id = commentRepository.save(comment).getId();

    boolean exists = commentRepository.existsById(id);

    assertThat(exists).isTrue();
  }

  @Test
  void 존재하지_않는_댓글() {
    UUID id = UUID.randomUUID();

    boolean exists = commentRepository.existsById(id);

    assertThat(exists).isFalse();
  }


  @Test
  void deleteById() {
    Comment comment = new Comment(
        articleId,
        UUID.randomUUID(),
        "테스트"
    );
    UUID id = commentRepository.save(comment).getId();

    commentRepository.deleteById(id);
    boolean exists = commentRepository.existsById(id);

    assertThat(exists).isFalse();
  }

  @Test
  void 존재하지_않는_댓글_삭제() {
    UUID id = UUID.randomUUID();
    assertThatCode(() -> commentRepository.deleteById(id)).doesNotThrowAnyException();
  }
}