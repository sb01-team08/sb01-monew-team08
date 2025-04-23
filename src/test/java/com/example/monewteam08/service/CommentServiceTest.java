package com.example.monewteam08.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.exception.comment.UnauthorizedCommentAccessException;
import com.example.monewteam08.mapper.CommentMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserRepository;
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
class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private CommentMapper commentMapper;

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
    mockComment = new Comment(articleId, userId, "테스트" );
    ReflectionTestUtils.setField(mockComment, "id", commentId);
  }

  @Test
  void 댓글_정상_등록() {
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(Article.class)));
    when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
    when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);
    when(commentMapper.toDto(any(Comment.class))).thenReturn(
        CommentDto.builder()
            .id(commentId.toString())
            .articleId(articleId.toString())
            .userId(userId.toString())
            .content("테스트" )
            .likeCount(0)
            .likedByMe(false)
            .userNickname(null)
            .createdAt(null)
            .build()
    );

    CommentDto result = commentService.create(request);

    assertNotNull(result);
    verify(commentRepository).save(any(Comment.class));
    assertEquals(commentId.toString(), result.getId());
    assertEquals(articleId.toString(), result.getArticleId());
    assertEquals(userId.toString(), result.getUserId());
    assertEquals("테스트", result.getContent());
  }

  @Test
  void 댓글_수정_테스트() {
    String newContent = "수정 댓글";
    CommentUpdateRequest updateRequest = new CommentUpdateRequest(newContent);

    when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(mockComment));
    when(commentMapper.toDto(any(Comment.class))).thenReturn(
        CommentDto.builder()
            .id(commentId.toString())
            .articleId(articleId.toString())
            .userId(userId.toString())
            .content(newContent)
            .likedByMe(false)
            .userNickname(null)
            .createdAt(null)
            .build()
    );
    CommentDto result = commentService.update(commentId, updateRequest, userId);

    assertEquals(newContent, mockComment.getContent());
    verify(commentRepository).findById(commentId);
    assertNotNull(mockComment.getUpdatedAt());
    assertNotNull(result);
  }

  @Test
  void 존재하지_않는_댓글_수정() {
    UUID userId = UUID.randomUUID();
    CommentUpdateRequest updateRequest = new CommentUpdateRequest("수정" );

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
}