package com.example.monewteam08.controller;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.dto.response.comment.CommentLikeDto;
import com.example.monewteam08.dto.response.comment.CursorPageResponseCommentDto;
import com.example.monewteam08.service.Interface.CommentLikeService;
import com.example.monewteam08.service.Interface.CommentService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final CommentLikeService commentLikeService;

  @GetMapping
  public ResponseEntity<CursorPageResponseCommentDto> getComments(
      @RequestParam(required = false) String articleId,
      @RequestParam String orderBy,
      @RequestParam String direction,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) String after,
      @RequestParam Integer limit,
      @RequestHeader("Monew-Request-User-ID") String requestUserId
  ) {
    CursorPageResponseCommentDto result = commentService.getCommentsByCursor(
        articleId, orderBy, direction, cursor, after, limit, requestUserId
    );
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<CommentDto> createComment(
      @RequestBody @Valid CommentRegisterRequest request) {
    CommentDto result = commentService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PostMapping("/{commentId}/comment-likes")
  public ResponseEntity<CommentLikeDto> like(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId
  ) {
    CommentLikeDto response = commentLikeService.like(userId, commentId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/{commentId}/comment-likes")
  public ResponseEntity<Void> unlike(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId) {
    commentLikeService.unlike(userId, commentId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{commentId}")
  public ResponseEntity<CommentDto> updateComment(
      @PathVariable UUID commentId,
      @RequestBody @Valid CommentUpdateRequest request,
      @RequestHeader("Monew-Request-User-ID") UUID userId
  ) {
    CommentDto result = commentService.update(commentId, request, userId);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId
  ) {
    commentService.delete(commentId, userId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{commentId}/hard")
  public ResponseEntity<Void> deleteComment(
      @PathVariable UUID commentId
  ) {
    commentService.delete_Hard(commentId);
    return ResponseEntity.noContent().build();
  }
}
