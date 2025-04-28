package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Article;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

  Page<Article> findAll(Specification<Article> spec, Pageable pageable);

  List<Article> findByPublishDate(LocalDate today);
}
