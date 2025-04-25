package com.example.monewteam08.repository;

import com.example.monewteam08.entity.ArticleView;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleViewRepository extends JpaRepository<ArticleView, UUID> {

  Optional<ArticleView> findByUserIdAndArticleId(UUID userId, UUID articleId);

  @Query("SELECT av.articleId FROM ArticleView av WHERE av.userId = :userId")
  List<UUID> findViewedArticleIds(UUID userId);

}
