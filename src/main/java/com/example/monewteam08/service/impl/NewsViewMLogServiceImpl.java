package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.NewsViewMLog;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.NewsViewMLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.service.Interface.NewsViewMLogService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsViewMLogServiceImpl implements NewsViewMLogService {

  private final UserActivityMongoRepository userActivityMongoRepository;
  private final NewsViewMLogMapper newsViewMLogMapper;
  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;

  @Override
  public void addArticleView(UUID userId, UUID articleId) {
    UserActivity userActivity = userActivityMongoRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));

    NewsViewMLog newsViewMLog = newsViewMLogMapper.toEntity(article, userId);

    List<NewsViewMLog> articles = userActivity.getArticleViews();
    articles.add(0, newsViewMLog);

    if (articles.size() > 10) {
      articles = articles.subList(0, 10);
    }

    userActivity.updateArticleViews(articles);
    userActivityMongoRepository.save(userActivity);
    log.info("뉴스 조회 로그 추가 완료: userId={}", userId);
  }

  @Override
  public List<NewsViewLogResponse> getNewsViewLogs(List<NewsViewMLog> newsViewMLogs) {
    Set<UUID> articleIds = newsViewMLogs.stream()
        .map(NewsViewMLog::getArticleId)
        .collect(Collectors.toSet());

    List<Object[]> commentCounts = commentRepository.countGroupByArticleIds(articleIds); // 직접 정의 필요
    Map<UUID, Long> commentCountMap = commentCounts.stream()
        .collect(Collectors.toMap(
            row -> (UUID) row[0],
            row -> (Long) row[1]
        ));

    Map<UUID, Article> articleMap = articleRepository.findAllById(articleIds).stream()
        .collect(Collectors.toMap(Article::getId, Function.identity()));

    return newsViewMLogs.stream().map(newsViewMLog -> {
      Article article = articleMap.get(newsViewMLog.getArticleId());
      if (article == null) {
        throw new ArticleNotFoundException(newsViewMLog.getArticleId());
      }
      int commentCount = commentCountMap.getOrDefault(newsViewMLog.getArticleId(), 0L).intValue();
      return newsViewMLogMapper.toResponse(newsViewMLog, commentCount,
          (int) article.getViewCount());
    }).collect(Collectors.toList());
  }
}
