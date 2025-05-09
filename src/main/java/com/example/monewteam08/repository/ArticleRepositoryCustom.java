package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Article;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ArticleRepositoryCustom {

  List<Article> findAllByCursor(
      String keyword,
      UUID interestId,
      List<String> sourceIn,
      LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo,
      String orderBy,
      String direction,
      String cursor,
      LocalDateTime after,
      int limit
  );

  long countAllByCondition(
      String keyword,
      UUID interestId,
      List<String> sourceIn,
      LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo
  );
}
