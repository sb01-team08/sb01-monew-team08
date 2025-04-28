package com.example.monewteam08.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.monewteam08.dto.response.article.ArticleViewDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.mapper.ArticleViewMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.ArticleViewRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.NewsViewLogService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleViewServiceImplTest {

  @Mock
  UserRepository userRepository;

  @Mock
  ArticleRepository articleRepository;

  @Mock
  ArticleViewRepository articleViewRepository;

  @Mock
  ArticleViewMapper articleViewMapper;

  @Mock
  NewsViewLogService newsViewLogService;

  @InjectMocks
  ArticleViewServiceImpl articleViewServiceImpl;

  @Test
  void 기사_뷰_저장_성공() {
    // given

    UUID userId = UUID.randomUUID();
    UUID articleId = UUID.randomUUID();
    Article article = spy(new Article("NAVER", "오늘의 경제 뉴스", "경제가 어렵습니다", "http://a.com",
        LocalDateTime.now(), null));
    User user = new User("test@test.com", "testUser", "password");
    Comment comment = new Comment(articleId, userId, "Great article!");
    ArticleViewDto articleViewDto = mock(ArticleViewDto.class);

    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(articleViewMapper.toDto(any(), any()))
        .willReturn(articleViewDto);

    // when
    ArticleViewDto result = articleViewServiceImpl.save(userId, articleId);

    // then
    assertNotNull(result);
    verify(article, times(1)).addViewCount();
  }

  @Test
  void 본인_조회_여부_확인_성공() {
    // given
    UUID userId = UUID.randomUUID();
    UUID articleId = UUID.randomUUID();

    given(articleViewRepository.findViewedArticleIds(userId))
        .willReturn(List.of(articleId));

    // when
    boolean result = articleViewServiceImpl.isViewedByUser(userId, articleId);

    // then
    assertNotNull(result);
    verify(articleViewRepository, times(1)).findViewedArticleIds(userId);
  }

}