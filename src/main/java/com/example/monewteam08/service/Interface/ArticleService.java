package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ArticleService {

  List<ArticleDto> fetchAndSave(UUID userId);

  CursorPageResponseArticleDto getArticles(String keyword,
      UUID interestId, List<String> sourceIn, LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo, String orderBy, String direction,
      String cursor, LocalDateTime after, Integer limit, UUID userId);

  void softDelete(UUID id);

  void hardDelete(UUID id);
}
