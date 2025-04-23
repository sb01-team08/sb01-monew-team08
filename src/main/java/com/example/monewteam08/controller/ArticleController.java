package com.example.monewteam08.controller;

import com.example.monewteam08.dto.response.article.ArticleViewDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.service.Interface.ArticleService;
import com.example.monewteam08.service.Interface.ArticleViewService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

  @DeleteMapping("/{articleId}")
  public ResponseEntity<Void> softDelete(@PathVariable UUID articleId) {
    try {
      articleService.softDelete(articleId);
      return ResponseEntity.noContent().build();
    } catch (ArticleNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/{articleId}/hard")
  public ResponseEntity<Void> hardDelete(@PathVariable UUID articleId) {
    try {
      articleService.hardDelete(articleId);
      return ResponseEntity.noContent().build();
    } catch (ArticleNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
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
      @RequestParam int limit,
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
        limit,
        monewRequestUserId
    );
    return ResponseEntity.ok(response);
  }

  @PostMapping("/{articleId}/article-views")
  public ResponseEntity<ArticleViewDto> registerArticleView(
      @PathVariable UUID articleId,
      @RequestHeader(name = "Monew-Request-User-Id") UUID userId
  ) {
    try {
      ArticleViewDto articleViewDto = articleViewService.save(userId, articleId);
      return ResponseEntity.ok(articleViewDto);
    } catch (ArticleNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
