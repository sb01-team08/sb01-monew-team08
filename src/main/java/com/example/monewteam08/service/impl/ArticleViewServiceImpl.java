package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.article.ArticleViewDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.ArticleView;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.ArticleViewMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.ArticleViewRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.ArticleViewService;
import com.example.monewteam08.service.Interface.NewsViewLogService;
import com.example.monewteam08.service.Interface.NewsViewMLogService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewServiceImpl implements ArticleViewService {

  private final ArticleViewRepository articleViewRepository;
  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final ArticleViewMapper articleViewMapper;

  private final NewsViewLogService newsViewLogService;
  private final NewsViewMLogService newsViewMLogService;

  @Transactional
  @Override
  public ArticleViewDto save(UUID userId, UUID articleId) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));
    userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    ArticleView articleView = articleViewRepository.findByUserIdAndArticleId(userId, articleId)
        .orElseGet(() -> {
          ArticleView newArticleView = new ArticleView(userId, articleId);
          article.addViewCount();
          articleRepository.save(article);
          return articleViewRepository.save(newArticleView);
        });
    return articleViewMapper.toDto(articleView, article);
  }

  @Override
  public boolean isViewedByUser(UUID userId, UUID articleId) {
    List<UUID> articleIds = articleViewRepository.findViewedArticleIds(userId);
    return articleIds.contains(articleId);
  }

}
