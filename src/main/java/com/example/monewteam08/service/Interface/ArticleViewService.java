package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.article.ArticleViewDto;
import java.util.UUID;

public interface ArticleViewService {

  ArticleViewDto save(UUID userId, UUID articleId);

}
