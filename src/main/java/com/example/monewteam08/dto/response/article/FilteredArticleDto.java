package com.example.monewteam08.dto.response.article;

import com.example.monewteam08.entity.Article;
import java.util.List;
import lombok.Getter;

@Getter
public class FilteredArticleDto {

  private List<Article> articles;
  private List<ArticleInterestCount> articleInterestCounts;

  public FilteredArticleDto(List<Article> articles,
      List<ArticleInterestCount> articleInterestCounts) {
    this.articles = articles;
    this.articleInterestCounts = articleInterestCounts;
  }
}
