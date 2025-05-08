package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Comment;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CommentRepositoryCustom {

  Optional<Comment> findById(UUID commentId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  int countByArticleId(UUID articleId);

  Optional<Comment> findByIdAndIsActiveTrue(UUID commentId);

  @Query("SELECT c.articleId, COUNT(c) FROM Comment c WHERE c.articleId IN :articleIds GROUP BY c.articleId")
  List<Object[]> countGroupByArticleIds(@Param("articleIds") Set<UUID> articleIds);
}
