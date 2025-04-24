package com.example.monewteam08.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.dto.response.comment.CommentLikeDto;
import com.example.monewteam08.dto.response.comment.CursorPageResponseCommentDto;
import com.example.monewteam08.exception.GlobalExceptionHandler;
import com.example.monewteam08.service.Interface.CommentLikeService;
import com.example.monewteam08.service.Interface.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = CommentController.class)
@Import(GlobalExceptionHandler.class)
class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CommentService commentService;

  @MockitoBean
  private CommentLikeService commentLikeService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 댓글_생성_성공() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID articleId = UUID.randomUUID();
    CommentRegisterRequest request = new CommentRegisterRequest(articleId.toString(),
        userId.toString(), "내용" );

    CommentDto response = CommentDto.builder()
        .id(UUID.randomUUID().toString())
        .articleId(articleId.toString())
        .userId(userId.toString())
        .userNickname("닉네임" )
        .content("내용" )
        .likeCount(0)
        .likedByMe(false)
        .createdAt(LocalDateTime.now())
        .build();

    when(commentService.create(any())).thenReturn(response);

    mockMvc.perform(post("/api/comments" )
            .header("Monew-Request-User-ID", userId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content" ).value("내용" ));
  }

  @Test
  void 댓글_페이지네이션_조회() throws Exception {
    CursorPageResponseCommentDto expected = CursorPageResponseCommentDto.builder()
        .content(new ArrayList<>())
        .nextCursor(null)
        .nextAfter(null)
        .size(0)
        .totalElements(0)
        .hasNext(false)
        .build();

    when(commentService.getCommentsByCursor(
        anyString(), anyString(), anyString(),
        any(), any(), anyInt(), anyString()
    )).thenReturn(expected);

    mockMvc.perform(get("/api/comments" )
            .param("articleId", UUID.randomUUID().toString())
            .param("orderBy", "createdAt" )
            .param("direction", "ASC" )
            .param("limit", "10" )
            .header("Monew-Request-User-ID", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size" ).value(0));
  }

  @Test
  void 댓글_수정() throws Exception {
    UUID commentId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    CommentDto dto = CommentDto.builder().id(commentId.toString()).content("수정" ).build();
    given(commentService.update(eq(commentId), any(CommentUpdateRequest.class),
        eq(userId))).willReturn(dto);

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/comments/" + commentId)
            .header("Monew-Request-User-ID", userId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new CommentUpdateRequest("수정" ))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content" ).value("수정" ));
  }

  @Test
  void 댓글_좋아요_생성() throws Exception {
    UUID commentId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    CommentLikeDto dto = CommentLikeDto.builder()
        .id(UUID.randomUUID().toString())
        .likedBy(userId.toString())
        .createdAt(LocalDateTime.now())
        .commentId(commentId.toString())
        .articleId(UUID.randomUUID().toString())
        .commentUserId(UUID.randomUUID().toString())
        .commentUserNickname("사용자" )
        .commentContent("댓글" )
        .commentLikeCount(1)
        .commentCreatedAt(LocalDateTime.now())
        .build();
    given(commentLikeService.like(commentId, userId)).willReturn(dto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments/" + commentId + "/comment-likes" )
            .header("Monew-Request-User-ID", userId))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.likedBy" ).value(userId.toString()));
  }

  @Test
  void 댓글_좋아요_삭제() throws Exception {
    UUID commentId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/" + commentId + "/comment-likes" )
            .header("Monew-Request-User-ID", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  void 댓글_논리삭제() throws Exception {
    UUID commentId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/" + commentId)
            .header("Monew-Request-User-ID", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  void 댓글_물리삭제() throws Exception {
    UUID commentId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/" + commentId + "/hard" )
            .header("Monew-Request-User-ID", userId))
        .andExpect(status().isNoContent());
  }
}