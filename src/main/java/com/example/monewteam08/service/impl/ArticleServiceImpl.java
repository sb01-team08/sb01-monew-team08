package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.ArticleInterestCount;
import com.example.monewteam08.dto.FilteredArticleDto;
import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.mapper.ArticleMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.SubscriptionRepository;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import com.example.monewteam08.service.Interface.ArticleService;
import com.example.monewteam08.service.Interface.ArticleViewService;
import com.example.monewteam08.service.Interface.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final ArticleFetchService articleFetchService;
  private final ArticleViewService articleViewService;
  private final SubscriptionRepository subscriptionRepository;
  private final InterestRepository interestRepository;
  private final NotificationService notificationService;
  private final CommentRepository commentRepository;
  private final ArticleMapper articleMapper;

  @Transactional
  @Override
  public List<ArticleDto> fetchAndSave(UUID userId) {
    Set<String> existingUrls = articleRepository.findAll().stream()
        .map(Article::getSourceUrl)
        .collect(Collectors.toSet());

    List<Article> allArticles = articleFetchService.fetchAllArticles();

    FilteredArticleDto filteredArticles;
    if (!existingUrls.isEmpty()) {
      List<Article> uniqueArticles = allArticles.stream()
          .filter(article -> !existingUrls.contains(article.getSourceUrl()))
          .toList();

      filteredArticles = filterWithKeywords(uniqueArticles, userId);
      filteredArticles.getArticleInterestCounts().forEach(interest ->
          notificationService.createArticleNotification(userId, interest.interestId(),
              interest.interestName(), interest.articleCount())
      );

    } else {
      filteredArticles = filterWithKeywords(allArticles, userId);
      filteredArticles.getArticleInterestCounts().forEach(interest ->
          notificationService.createArticleNotification(userId, interest.interestId(),
              interest.interestName(), interest.articleCount())
      );
    }

    List<Article> savedArticles = articleRepository.saveAll(filteredArticles.getArticles());

    return savedArticles.stream()
        .map(article -> articleMapper.toDto(article, 0L, false))
        .toList();
  }

  @Override
  public CursorPageResponseArticleDto getArticles(String keyword,
      UUID interestId, List<String> sourceIn, LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo, String orderBy, String direction,
      String cursor, LocalDateTime after, int limit, UUID userId) {

    Pageable pageable = createPageable(limit, orderBy, direction, "publishDate", "desc");
    Specification<Article> spec = getSpec(keyword, interestId, sourceIn, publishDateFrom,
        publishDateTo, after);

    Page<Article> articles = articleRepository.findAll(spec, pageable);

    List<ArticleDto> articleDtos = articles.stream()
        .map(article -> {
          long commentCount = commentRepository.countByArticleId(article.getId());
          boolean viewedByMe = articleViewService.isViewedByUser(userId, article.getId());
          return articleMapper.toDto(article, commentCount, viewedByMe);
        }).toList();

    String nextCursor = null;
    LocalDateTime nextAfter = null;

    if (articles.hasNext()) {
      LocalDateTime lastPublishedAt = articles.getContent().get(articles.getNumberOfElements() - 1)
          .getPublishDate();
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

  @Transactional
  @Override
  public void softDelete(UUID id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new ArticleNotFoundException(id));
    article.softDelete();
    articleRepository.save(article);
  }

  @Transactional
  @Override
  public void hardDelete(UUID id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new ArticleNotFoundException(id));
    articleRepository.delete(article);
  }

  protected FilteredArticleDto filterWithKeywords(List<Article> articles, UUID userId) {
    List<UUID> interestIds = subscriptionRepository.findAll().stream()
        .filter(subscription -> subscription.getUserId() == userId)
        .map(Subscription::getInterestId)
        .toList();

    List<ArticleInterestCount> articleInterestCounts = countArticleByInterest(articles,
        interestIds);

    if (interestIds.isEmpty()) {
      return new FilteredArticleDto(articles, articleInterestCounts);
    }

    List<String> keywords = interestIds.stream()
        .flatMap(interestId -> interestRepository.findById(interestId).stream())
        .flatMap(interest -> interest.getKeywords().stream())
        .toList();

    List<Article> filteredArticles = articles.stream()
        .filter(article -> keywords.stream()
            .anyMatch(keyword ->
                article.getTitle().contains(keyword) || article.getSummary().contains(keyword)))
        .toList();

    return new FilteredArticleDto(filteredArticles, articleInterestCounts);
  }

  protected List<ArticleInterestCount> countArticleByInterest(List<Article> articles,
      List<UUID> interestIds) {
    List<Interest> interests = interestRepository.findAllById(interestIds);

    return interests.stream()
        .map(interest -> {
          Long count = articles.stream().filter(article -> interest.getKeywords().stream()
                  .anyMatch(keyword -> article.getTitle().contains(keyword) || article.getSummary()
                      .contains(keyword)))
              .count();
          return new ArticleInterestCount(interest.getId(), interest.getName(), count.intValue());
        })
        .toList();
  }

  private Pageable createPageable(int limit, String sortField, String sortDirection,
      String defaultSortField, String defaultSortDirection) {
    if (limit <= 0) {
      limit = 10;
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
    Specification<Article> spec = (root, query, cb) -> cb.isTrue(root.get("isActive"));

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
          (root, query, cb) -> cb.between(root.get("publishDate"), publishDateFrom, publishDateTo));
    } else if (publishDateFrom != null && publishDateTo == null) {
      spec = spec.and(
          (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("publishDate"), publishDateFrom));
    } else if (publishDateFrom == null && publishDateTo != null) {
      spec = spec.and(
          (root, query, cb) -> cb.lessThanOrEqualTo(root.get("publishDate"), publishDateTo));
    }

    if (after != null) {
      spec = spec.and(
          (root, query, cb) -> cb.lessThan(root.get("publishDate"), after));
    }

    return spec;
  }


}
