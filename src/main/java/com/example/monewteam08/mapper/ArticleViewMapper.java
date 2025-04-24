package com.example.monewteam08.mapper;

import com.example.monewteam08.dto.response.article.ArticleViewDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.ArticleView;
import org.springframework.stereotype.Component;

@Component
public class ArticleViewMapper {

  public ArticleViewDto toDto(ArticleView articleView, Article article, long commentCount) {

    return new ArticleViewDto(
        articleView.getId(),
        articleView.getUserId(),
        articleView.getViewedAt(),
        article.getId(),
        article.getSource(),
        article.getSourceUrl(),
        article.getTitle(),
        article.getPublishDate(),
        article.getSummary(),
        commentCount,
        article.getViewCount()
    );
  }
}
