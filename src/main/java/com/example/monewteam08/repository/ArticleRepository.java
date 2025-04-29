package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Article;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, UUID>, ArticleRepositoryCustom {

  List<Article> findAllByPublishDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
