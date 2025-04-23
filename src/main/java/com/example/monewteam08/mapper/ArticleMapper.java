package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

  public ArticleDto toDto(Article article, boolean viewedByMe) {
    return new ArticleDto(
        article.getId(),
        article.getSource(),
        article.getSourceUrl(),
        article.getTitle(),
        article.getPublishedAt(),
        article.getSummary(),
        article.getViewCount(),
        viewedByMe
    );
  }
}
