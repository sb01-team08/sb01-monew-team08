package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import java.util.List;
import java.util.UUID;

public interface ArticleService {

  List<ArticleDto> save();

  CursorPageResponseArticleDto getArticles();

  void softDelete(UUID id);

  void hardDelete(UUID id);
}
