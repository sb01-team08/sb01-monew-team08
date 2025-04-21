package com.example.monewteam08.dto.response.article;

import java.time.LocalDateTime;
import java.util.UUID;

public class ArticleDto {

  private UUID id;
  private String source;
  private String sourceUrl;
  private String title;
  private LocalDateTime publishedAt;
  private String summary;
  private long viewCount;
  private boolean viewedByMe;
}
