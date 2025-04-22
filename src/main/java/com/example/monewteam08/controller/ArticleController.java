package com.example.monewteam08.controller;

import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.service.Interface.ArticleService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

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
}
