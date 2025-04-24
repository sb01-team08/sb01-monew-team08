package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.NewsViewLog;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.mapper.NewsViewLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.ArticleViewRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.NewsViewLogRepository;
import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.service.Interface.NewsViewLogService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsViewLogServiceImpl implements NewsViewLogService {

  private final UserActivityLogRepository userActivityLogRepository;
  private final NewsViewLogMapper newsViewLogMapper;
  private final NewsViewLogRepository newsViewLogRepository;

  private static final int LIMIT_SIZE = 10;
  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;
  private final ArticleViewRepository articleViewRepository;

  @Override
  public void addNewsViewLog(UUID userId, Article article) {
    log.debug("뉴스 조회 로그 추가 요청: userId={}", userId);
    // todo: exception
    UserActivityLog userActivityLog = userActivityLogRepository.findByUserId(userId).orElseThrow();
    NewsViewLog newsViewLog = newsViewLogMapper.toEntity(userActivityLog, article, userId);

    newsViewLogRepository.save(newsViewLog);
    log.info("뉴스 조회 로그 생성 완료: userId={}, logId={}", userId, newsViewLog.getId());
  }

  @Override
  public void removeNewsViewLog(UUID userId, UUID articleId) {
    // todo: 추후 뉴스 뷰 삭제 부분에 UserId 필요하다구 하기
    log.debug("뉴스 조회 로그 삭제 요청: userId={}", userId);
    newsViewLogRepository.deleteNewsViewLogByCommentIdAndUserId(userId, articleId);
    log.info("뉴스 조회 로그 삭제 완료: userId={}, articleId={}", userId, articleId);
  }

  @Override
  public List<NewsViewLogResponse> getNewsViewLogs(UserActivityLog userActivityLog) {
    log.debug("뉴스 조회 로그 조회 요청: userActivityId={}", userActivityLog.getId());
    List<NewsViewLog> newsViewLogs = newsViewLogRepository.getNewsViewLogsByActivityLogOrderByCreatedAtDesc(
        userActivityLog,
        PageRequest.of(0, LIMIT_SIZE));

    List<NewsViewLogResponse> newsViewLogResponses = newsViewLogs.stream()
        .map(newsViewLog -> {
          Article article = articleRepository.findById(newsViewLog.getArticleId())
              .orElseThrow(() -> new ArticleNotFoundException(newsViewLog.getArticleId()));
          long articleViewCount = article.getViewCount();
          int articleCommentCount = commentRepository.countByArticleId(newsViewLog.getArticleId());

          return newsViewLogMapper.toResponse(newsViewLog, articleCommentCount,
              (int) articleViewCount);
        })
        .toList();
    log.info("뉴스 조회 로그 조회 완료");
    return newsViewLogResponses;
  }
}
