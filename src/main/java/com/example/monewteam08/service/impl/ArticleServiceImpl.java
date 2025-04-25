package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.ArticleInterestCount;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.dto.response.article.FilteredArticleDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.entity.Subscription;
import com.example.monewteam08.exception.Interest.InterestNotFoundException;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.mapper.ArticleMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.ArticleRepositoryCustom;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.SubscriptionRepository;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import com.example.monewteam08.service.Interface.ArticleService;
import com.example.monewteam08.service.Interface.ArticleViewService;
import com.example.monewteam08.service.Interface.NewsViewLogService;
import com.example.monewteam08.service.Interface.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final ArticleRepositoryCustom articleRepositoryCustom;
  private final ArticleMapper articleMapper;
  private final NewsViewLogService newsViewLogService;

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
        .map(article -> articleMapper.toDto(article, false))
        .toList();
  }

  @Override
  public CursorPageResponseArticleDto getArticles(String keyword,
      UUID interestId, List<String> sourceIn, LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo, String orderBy, String direction,
      String cursor, LocalDateTime after, int limit, UUID userId) {

    List<Article> articles = articleRepositoryCustom.findAllByCursor(
        keyword,
        interestId,
        sourceIn,
        publishDateFrom,
        publishDateTo,
        orderBy,
        direction,
        cursor,
        after != null ? after.toString() : null,
        limit
    );

    List<ArticleDto> articleDtos = articles.stream()
        .map(article -> {
          boolean viewedByMe = articleViewService.isViewedByUser(userId, article.getId());
          return articleMapper.toDto(article, viewedByMe);
        }).toList();

    String nextCursor = null;
    LocalDateTime nextAfter = null;

    if (articles.size() > limit) {
      Article last = articles.get(limit);
      nextAfter = last.getPublishDate();
      switch (orderBy) {
        case "commentCount" -> nextCursor = String.valueOf(last.getComments().size());
        case "viewCount" -> nextCursor = String.valueOf(last.getViewCount());
        default -> nextCursor = last.getPublishDate().toString();
      }
    }

    long totalElements = articleRepositoryCustom.countAllByCondition(
        keyword,
        interestId,
        sourceIn,
        publishDateFrom,
        publishDateTo
    );

    return new CursorPageResponseArticleDto(
        articleDtos,
        nextCursor,
        nextAfter,
        articleDtos.size(),
        totalElements,
        articles.size() > limit
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
        .filter(subscription -> subscription.getUserId().equals(userId))
        .map(Subscription::getInterestId)
        .toList();

    log.debug("User 관심사 id 목록: {}", interestIds);

    List<ArticleInterestCount> articleInterestCounts = countArticleByInterest(articles,
        interestIds);

    if (interestIds.isEmpty()) {
      interestIds = interestRepository.findAll().stream()
          .map(Interest::getId)
          .toList();
    }
    if (interestIds.isEmpty()) {
      return new FilteredArticleDto(articles, List.of());
    }

    Map<UUID, List<String>> interestIdAndKeywords = interestIds.stream()
        .map(interestId -> interestRepository.findById(interestId)
            .orElseThrow(() -> new InterestNotFoundException(interestId.toString())))
        .collect(Collectors.toMap(Interest::getId, Interest::getKeywords));

    List<Article> filteredArticles = articles.stream()
        .filter(article -> {
          boolean matched = false;
          for (Map.Entry<UUID, List<String>> entry : interestIdAndKeywords.entrySet()) {
            UUID interestId = entry.getKey();
            List<String> keywords = entry.getValue();
            boolean containsKeyword = keywords.stream().anyMatch(keyword ->
                article.getTitle().contains(keyword) || article.getSummary().contains(keyword)
            );
            if (containsKeyword) {
              article.setInterestId(interestId);
              matched = true;
            }
          }

          return matched;
        })
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

}
