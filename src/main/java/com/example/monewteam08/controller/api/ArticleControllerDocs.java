package com.example.monewteam08.controller.api;

import com.example.monewteam08.dto.response.article.ArticleRestoreResultDto;
import com.example.monewteam08.dto.response.article.ArticleViewDto;
import com.example.monewteam08.dto.response.article.CursorPageResponseArticleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "뉴스 기사 관리", description = "뉴스 기사 관리 API")
@RequestMapping("/api/articles")
public interface ArticleControllerDocs {

  @Operation(summary = "기사 논리 삭제", description = "기사를 논리적으로 석제합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "논리 삭제 성공"
      ),
      @ApiResponse(
          responseCode = "404", description = "기사를 찾을 수 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  ResponseEntity<Void> softDelete(@PathVariable UUID articleId);

  @Operation(summary = "기사 물리 삭제", description = "기사를 물리적으로 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "물리 삭제 성공"
      ),
      @ApiResponse(
          responseCode = "404", description = "기사를 찾을 수 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  ResponseEntity<Void> hardDelete(@PathVariable UUID articleId);

  @Operation(summary = "기사 목록 조회", description = "조건에 맞는 기사 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseArticleDto.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청 (정렬 기준 오류, 페이지네이션 파라미터 오류 등)",
          content = @Content(schema = @Schema(implementation = CursorPageResponseArticleDto.class))
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류",
          content = @Content(schema = @Schema(implementation = CursorPageResponseArticleDto.class))
      )
  })
  ResponseEntity<CursorPageResponseArticleDto> getArticles(
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
      @RequestHeader(name = "Monew-Request-User-Id") UUID monewRequestUserId);

  @Operation(summary = "기사 뷰 등록", description = "기사 뷰를 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "기사 뷰 등록 성공",
          content = @Content(schema = @Schema(implementation = ArticleViewDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "기사를 찾을 수 없음"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  ResponseEntity<ArticleViewDto> registerArticleView(
      @PathVariable UUID articleId,
      @RequestHeader(name = "Monew-Request-User-Id") UUID userId
  );

  @Operation(summary = "뉴스 복구", description = "유실된 뉴스를 복구합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "복구 성공"
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 내부 오류"
      )
  })
  ResponseEntity<ArticleRestoreResultDto> restore(
      @RequestParam LocalDateTime from,
      @RequestParam LocalDateTime to
  );

}
