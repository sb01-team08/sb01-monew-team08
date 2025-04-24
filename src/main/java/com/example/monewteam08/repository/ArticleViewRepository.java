package com.example.monewteam08.repository;

import com.example.monewteam08.entity.ArticleView;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewRepository extends JpaRepository<ArticleView, UUID> {

  Optional<ArticleView> findByUserIdAndArticleId(UUID userId, UUID articleId);
}
