package com.example.monewteam08.controller;

import com.example.monewteam08.dto.response.article.ArticleDto;
import com.example.monewteam08.dto.response.article.ArticleViewDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.service.Interface.ArticleService;
import com.example.monewteam08.service.Interface.ArticleViewService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;
  private final ArticleViewService articleViewService;

  // 테스트용: 즉시 기사 불러오기
  @PostMapping("/fetch")
  public ResponseEntity<List<ArticleDto>> fetchAndSave(
      @RequestHeader(name = "Monew-Request-User-ID") UUID userId
  ) {
    List<ArticleDto> articles = articleService.fetchAndSave(userId);
    return ResponseEntity.ok(articles);
  }

  @DeleteMapping("/{articleId}")
  public ResponseEntity<Void> softDelete(@PathVariable UUID articleId) {
    articleService.softDelete(articleId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{articleId}/hard")
  public ResponseEntity<Void> hardDelete(@PathVariable UUID articleId) {
    articleService.hardDelete(articleId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseArticleDto> getArticles(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) UUID interestId,
      @RequestParam(required = false) List<String> sourceIn,
      @RequestParam(required = false) LocalDateTime publishDateFrom,
      @RequestParam(required = false) LocalDateTime publishDateTo,
      @RequestParam String orderBy,
      @RequestParam String direction,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) LocalDateTime after, //createdAt
      @RequestParam(defaultValue = "10") Integer limit,
      @RequestHeader(name = "Monew-Request-User-Id") UUID monewRequestUserId
  ) {
    CursorPageResponseArticleDto response = articleService.getArticles(
        keyword,
        interestId,
        sourceIn,
        publishDateFrom,
        publishDateTo,
        orderBy,
        direction,
        cursor,
        after,
        10,
        monewRequestUserId
    );
    return ResponseEntity.ok(response);
  }

  @PostMapping("/{articleId}/article-views")
  public ResponseEntity<ArticleViewDto> registerArticleView(
      @PathVariable UUID articleId,
      @RequestHeader(name = "Monew-Request-User-Id") UUID userId
  ) {
    ArticleViewDto articleViewDto = articleViewService.save(userId, articleId);
    return ResponseEntity.ok(articleViewDto);
  }
}
