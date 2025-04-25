package com.example.monewteam08.controller.api;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.dto.response.comment.CommentLikeDto;
import com.example.monewteam08.dto.response.comment.CursorPageResponseCommentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
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

@Tag(name = "댓글 관리", description = "댓글 관리 API")
@RequestMapping("/api/comments")
public interface CommentControllerDocs {

  @Operation(summary = "댓글 목록 조회", description = "조건에 맞는 댓글 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseCommentDto.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청 (정렬 기준 오류, 페이지네이션 파라미터 오류 등)"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @GetMapping
  ResponseEntity<CursorPageResponseCommentDto> getComments(
      @RequestParam(required = false) String articleId,
      @RequestParam String orderBy,
      @RequestParam String direction,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) String after,
      @RequestParam Integer limit,
      @RequestHeader("Monew-Request-User-ID") String requestUserId
  );

  @Operation(summary = "댓글 등록", description = "새로운 댓글을 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CommentRegisterRequest.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청 (입력값 검증 실패)"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @PostMapping
  ResponseEntity<CommentDto> createComment(
      @RequestBody @Valid CommentRegisterRequest request);

  @Operation(summary = "좋아요", description = "댓글 좋아요를 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "댓글 좋아요 성공",
          content = @Content(schema = @Schema(implementation = CommentLikeDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "댓글 정보 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @PostMapping("/{commentId}/comment-likes")
  ResponseEntity<CommentLikeDto> like(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId
  );

  @Operation(summary = "좋아요 취소", description = "댓글 좋아요를 취소합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "댓글 좋아요 취소 성공"
      ),
      @ApiResponse(
          responseCode = "404", description = "댓글 정보 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @DeleteMapping("/{commentId}/comment-likes")
  ResponseEntity<Void> unlike(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId);

  @Operation(summary = "댓글 정보 수정", description = "댓글 내용을 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = CommentUpdateRequest.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청(입력값 검증 실패)"
      ),
      @ApiResponse(
          responseCode = "404", description = "댓글 정보 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @PatchMapping("/{commentId}")
  ResponseEntity<CommentDto> updateComment(
      @PathVariable UUID commentId,
      @RequestBody @Valid CommentUpdateRequest request,
      @RequestHeader("Monew-Request-User-ID") UUID userId
  );

  @Operation(summary = "댓글 논리 삭제", description = "댓글을 논리적으로 삭제 합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "댓글 삭제 성공"
      ),
      @ApiResponse(
          responseCode = "404", description = "댓글 정보 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @DeleteMapping("/{commentId}")
  ResponseEntity<Void> deleteComment(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId
  );

  @Operation(summary = "댓글 물리 삭제", description = "댓글을 물리적으로 삭제 합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "댓글 삭제 성공"
      ),
      @ApiResponse(
          responseCode = "404", description = "댓글 정보 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  @DeleteMapping("/{commentId}/hard")
  ResponseEntity<Void> deleteComment(
      @PathVariable UUID commentId
  );
}
