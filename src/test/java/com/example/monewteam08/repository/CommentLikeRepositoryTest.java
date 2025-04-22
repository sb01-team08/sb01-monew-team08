package com.example.monewteam08.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.CommentLike;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
class CommentLikeRepositoryTest {

  @Autowired
  private CommentLikeRepository commentLikeRepository;

  @Test
  void saveAndFindById() {
    UUID userId = UUID.randomUUID();
    UUID commentId = UUID.randomUUID();
    CommentLike like = new CommentLike(userId, commentId);

    CommentLike save = commentLikeRepository.save(like);

    Optional<CommentLike> result = commentLikeRepository.findById(like.getId());
    assertThat(result).isPresent();
    assertThat(result.get().getUserId()).isEqualTo(userId);
    assertThat(result.get().getCommentId()).isEqualTo(commentId);
  }

  @Test
  void deleteByUserIdAndCommentId() {
    // given
    UUID userId = UUID.randomUUID();
    UUID commentId = UUID.randomUUID();
    CommentLike like = new CommentLike(userId, commentId);
    commentLikeRepository.save(like);

    // when
    commentLikeRepository.deleteByUserIdAndCommentId(userId, commentId);

    // then
    boolean exists = commentLikeRepository.findAll()
        .iterator().hasNext();
    assertThat(exists).isFalse();
  }
}