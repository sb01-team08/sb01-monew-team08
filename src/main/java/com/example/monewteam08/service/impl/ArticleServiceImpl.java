package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.mapper.ArticleMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import com.example.monewteam08.service.Interface.ArticleService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final ArticleFetchService articleFetchService;
  private final ArticleMapper articleMapper;

  @Override
  public List<ArticleDto> save() {
    List<Article> articles = articleFetchService.fetchAllArticles();
    List<Article> filteredArticles = filterWithKeywords(articles);
    List<Article> savedArticles = articleRepository.saveAll(filteredArticles);

    return savedArticles.stream()
        .map(article -> articleMapper.toDto(article, false))
        .toList();
  }

  @Override
  public CursorPageResponseArticleDto getArticles() {

    return null;
  }

  @Override
  public void softDelete(UUID id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new ArticleNotFoundException(id));
    article.softDelete();
    articleRepository.save(article);
  }

  @Override
  public void hardDelete(UUID id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new ArticleNotFoundException(id));
    articleRepository.delete(article);
  }

  protected List<Article> filterWithKeywords(List<Article> articles) {
    List<String> keywords = List.of("경제"); // 관심사 서비스 반영 예정

    return articles.stream()
        .filter(article -> keywords.stream()
            .anyMatch(keyword ->
                article.getTitle().contains(keyword) || article.getSummary().contains(keyword)))
        .toList();
  }


}
