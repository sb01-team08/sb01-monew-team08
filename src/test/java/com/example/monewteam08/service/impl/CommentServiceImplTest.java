package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.dto.response.comment.CursorPageResponseCommentDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.exception.comment.UnauthorizedCommentAccessException;
import com.example.monewteam08.mapper.CommentMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentLikeRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentLikeMLogService;
import com.example.monewteam08.service.Interface.CommentMLogService;
import com.example.monewteam08.service.Interface.NewsViewMLogService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private CommentLikeRepository commentLikeRepository;

  @Mock
  private CommentLikeMLogService commentLikeLogService;

  @Mock
  private CommentMLogService commentRecentLogService;

  @Mock
  private CommentMapper commentMapper;

  @Mock
  private NewsViewMLogService newsViewMLogService;

  @InjectMocks
  private CommentServiceImpl commentService;

  private UUID articleId;
  private UUID userId;
  private UUID commentId;
  private CommentRegisterRequest request;
  private Comment mockComment;

  @BeforeEach
  void setUp() {
    articleId = UUID.randomUUID();
    userId = UUID.randomUUID();
    commentId = UUID.randomUUID();

    request = new CommentRegisterRequest(
        articleId.toString(),
        userId.toString(),
        "테스트"
    );
    mockComment = new Comment(articleId, userId, "테스트");
    ReflectionTestUtils.setField(mockComment, "id", commentId);
  }

  @Test
  void 댓글_정상_등록() {
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(Article.class)));

    User mockUser = mock(User.class);
    when(mockUser.getNickname()).thenReturn("닉네임");
    when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

    when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

    when(commentMapper.toDto(any(Comment.class), eq("닉네임"), eq(false))).thenReturn(
        CommentDto.builder()
            .id(commentId.toString())
            .articleId(articleId.toString())
            .userId(userId.toString())
            .content("테스트")
            .likeCount(0)
            .likedByMe(false)
            .userNickname("닉네임")
            .createdAt(null)
            .build()
    );

    Article mockArticle = mock(Article.class);
    when(mockArticle.getTitle()).thenReturn("테스트 제목");
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(mockArticle));

    CommentDto result = commentService.create(request);

    assertNotNull(result);
    verify(commentRepository).save(any(Comment.class));
    assertEquals(commentId.toString(), result.getId());
    assertEquals(articleId.toString(), result.getArticleId());
    assertEquals(userId.toString(), result.getUserId());
    assertEquals("테스트", result.getContent());
    assertEquals("닉네임", result.getUserNickname());
    assertFalse(result.isLikedByMe());
    verify(commentRecentLogService).addRecentComment(any(User.class), any(Comment.class),
        anyString());
  }

  @Test
  void 댓글_수정_테스트() {
    String newContent = "수정 댓글";
    CommentUpdateRequest updateRequest = new CommentUpdateRequest(newContent);

    when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(mockComment));
    when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
    when(commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)).thenReturn(true);

    when(commentMapper.toDto(any(Comment.class), any(), eq(true))).thenReturn(
        CommentDto.builder()
            .id(commentId.toString())
            .articleId(articleId.toString())
            .userId(userId.toString())
            .content(newContent)
            .likedByMe(true)
            .userNickname("닉네임")
            .createdAt(null)
            .build()
    );
    CommentDto result = commentService.update(commentId, updateRequest, userId);

    assertEquals(newContent, mockComment.getContent());
    verify(commentRepository).findById(commentId);
    assertNotNull(mockComment.getUpdatedAt());
    assertNotNull(result);
    assertEquals("닉네임", result.getUserNickname());
    assertTrue(result.isLikedByMe());
  }

  @Test
  void 존재하지_않는_댓글_수정() {
    UUID userId = UUID.randomUUID();
    CommentUpdateRequest updateRequest = new CommentUpdateRequest("수정");

    when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

    assertThrows(CommentNotFoundException.class, () -> {
      commentService.update(commentId, updateRequest, userId);
    });
  }

  @Test
  void 댓글_정상_논리_삭제() {
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
    commentService.delete(commentId, userId);
    assertEquals(false, mockComment.isActive());
  }

  @Test
  void 댓글_정상_물리_삭제() {
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
    commentService.delete_Hard(commentId);
    then(commentRepository).should().deleteById(commentId);
  }

  @Test
  void 존재하지_않는_댓글_삭제_불가() {
    when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
    assertThrows(CommentNotFoundException.class, () -> {
      commentService.delete(commentId, userId);
    });
  }

  @Test
  void 본인이_아닌_경우_삭제_불가() {
    UUID otherUserId = UUID.randomUUID();
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
    assertThrows(UnauthorizedCommentAccessException.class, () -> {
      commentService.delete(commentId, otherUserId);
    });
  }

  @Test
  void 존재하지_않는_기사_댓글등록_실패() {
    when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

    assertThrows(ArticleNotFoundException.class, () -> {
      commentService.create(request);
    });
  }

  @Test
  void 다른_사용자_댓글_수정_실패() {
    UUID otherUserId = UUID.randomUUID();
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

    assertThrows(UnauthorizedCommentAccessException.class, () -> {
      commentService.update(commentId, new CommentUpdateRequest("수정"), otherUserId);
    });
  }

  @Test
  void 커서_기반_댓글_조회_성공() {
    UUID commentId = UUID.randomUUID();
    Comment comment = new Comment(articleId, userId, "테스트 댓글");
    ReflectionTestUtils.setField(comment, "id", commentId);

    when(commentRepository.findAllByCursor(any(), any(), any(), any(), any(), anyInt()))
        .thenReturn(List.of(comment));

    User user = new User("user@naver.com", "우디", "123qwe!@");
    ReflectionTestUtils.setField(user, "id", userId);

    when(userRepository.findByIdIn(any())).thenReturn(List.of(user));
    when(commentLikeRepository.findAllByUserIdAndCommentIdIn(any(), any()))
        .thenReturn(List.of());

    when(commentRepository.countByArticleId(articleId)).thenReturn(1);

    CursorPageResponseCommentDto result = commentService.getCommentsByCursor(
        articleId.toString(), "createdAt", "desc", null, null, 10, userId.toString()
    );

    assertNotNull(result);
    assertThat(result.getContent()).hasSize(1);
    //assertFalse(result.getHasNext());
  }

  @Test
  void 댓글_생성시_최신댓글로그_추가() {
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(Article.class)));
    when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
    when(commentRepository.save(any())).thenReturn(mockComment);

    Article mockArticle = mock(Article.class);
    when(mockArticle.getTitle()).thenReturn("테스트 제목");
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(mockArticle));

    commentService.create(request);

    verify(commentRecentLogService).addRecentComment(any(User.class), any(Comment.class),
        anyString());
  }

  @Test
  void 댓글_논리삭제시_최신댓글로그_삭제() {
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

    commentService.delete(commentId, userId);
  }

  @Test
  void 댓글_물리삭제시_최신댓글로그_삭제() {
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

    commentService.delete_Hard(commentId);
  }
}