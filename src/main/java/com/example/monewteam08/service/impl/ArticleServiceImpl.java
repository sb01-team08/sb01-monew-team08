package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.mapper.ArticleMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import com.example.monewteam08.service.Interface.ArticleService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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
  public CursorPageResponseArticleDto getArticles(String keyword,
      UUID interestId, List<String> sourceIn, LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo, String orderBy, String direction,
      String cursor, LocalDateTime after, int limit, UUID userId) {

    Pageable pageable = createPageable(limit, orderBy, direction, "publishedAt", "desc");
    Specification<Article> spec = getSpec(keyword, interestId, sourceIn, publishDateFrom,
        publishDateTo, after);

    Page<Article> articles = articleRepository.findAllByIsActiveTrue(spec, pageable);

    List<ArticleDto> articleDtos = articles.stream()
        .map(article -> articleMapper.toDto(article,
            false)) //ArticleView에서 userId 있는지 확인 추가 필요, 임시로 false 처리
        .toList();

    String nextCursor = null;
    LocalDateTime nextAfter = null;

    if (articles.hasNext()) {
      LocalDateTime lastPublishedAt = articles.getContent().get(articles.getNumberOfElements() - 1)
          .getPublishedAt();
      nextCursor = lastPublishedAt.toString();
      nextAfter = lastPublishedAt;
    }

    return new CursorPageResponseArticleDto(
        articleDtos,
        nextCursor,
        nextAfter,
        articles.getSize(),
        articles.getTotalElements(),
        articles.hasNext()
    );
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

  private Pageable createPageable(int limit, String sortField, String sortDirection,
      String defaultSortField, String defaultSortDirection) {
    if (limit <= 0) {
      limit = 50;
    }

    Sort sort;
    Direction direction;
    if (sortField != null && !sortField.isEmpty()) {
      direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
      sort = Sort.by(direction, sortField);
    } else {
      direction =
          "desc".equalsIgnoreCase(defaultSortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
      sort = Sort.by(direction, defaultSortField);
    }

    return PageRequest.of(0, limit, sort);
  }

  private Specification<Article> getSpec(String keyword, UUID interestId,
      List<String> sourceIn, LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo, LocalDateTime after) {
    Specification<Article> spec = Specification.where(null);

    if (keyword != null) {
      spec = spec.and((root, query, cb) -> cb.or(
          cb.like(root.get("title"), "%" + keyword + "%"),
          cb.like(root.get("summary"), "%" + keyword + "%")
      ));
    }
    if (interestId != null) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("interestId"), interestId));
    }

    if (sourceIn != null && !sourceIn.isEmpty()) {
      spec = spec.and((root, query, cb) -> root.get("source").in(sourceIn));
    }

    if (publishDateFrom != null && publishDateTo != null) {
      spec = spec.and(
          (root, query, cb) -> cb.between(root.get("publishedAt"), publishDateFrom, publishDateTo));
    } else if (publishDateFrom != null && publishDateTo == null) {
      spec = spec.and(
          (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("publishedAt"), publishDateFrom));
    } else if (publishDateFrom == null && publishDateTo != null) {
      spec = spec.and(
          (root, query, cb) -> cb.lessThanOrEqualTo(root.get("publishedAt"), publishDateTo));
    }

    if (after != null) {
      spec = spec.and(
          (root, query, cb) -> cb.lessThan(root.get("publishedAt"), after));
    }

    return spec;
  }


}
