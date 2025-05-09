package com.example.monewteam08.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.dto.response.comment.CommentLikeDto;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.mapper.CommentLikeMapper;
import com.example.monewteam08.repository.CommentLikeRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentLikeLogService;
import com.example.monewteam08.service.Interface.CommentLikeMLogService;
import com.example.monewteam08.service.Interface.NotificationService;
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
class CommentLikeServiceImplTest {

  @Mock
  private CommentLikeRepository commentLikeRepository;
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private CommentLikeMapper commentLikeMapper;
  @Mock
  private UserRepository userRepository;
  @Mock
  private CommentLikeLogService commentLikeLogService;
  @Mock
  private NotificationService notificationService;
  @Mock
  private CommentLikeMLogService commentLikeMLogService;

  @InjectMocks
  private CommentLikeServiceImpl commentLikeService;

  private UUID commentId;
  private UUID userId;
  private Comment mockComment;
  private User mockUser;

  @BeforeEach
  void setUp() {
    commentId = UUID.randomUUID();
    userId = UUID.randomUUID();
    mockUser = new User("test@example.com", "nickname", "password");
    mockComment = new Comment(UUID.randomUUID(), UUID.randomUUID(), "내용");
    ReflectionTestUtils.setField(mockComment, "id", commentId);
  }

  @Test
  void 좋아요_등록_성공() {
    CommentLike like = new CommentLike(userId, commentId);
    CommentLikeDto dto = CommentLikeDto.builder()
        .id(UUID.randomUUID().toString())
        .commentId(commentId.toString())
        .likedBy(userId.toString())
        .commentLikeCount(1)
        .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
    when(commentRepository.findByIdAndIsActiveTrue(commentId)).thenReturn(Optional.of(mockComment));
    when(commentLikeRepository.save(any())).thenReturn(like);
    when(commentLikeMapper.toDto(any(), any(), any())).thenReturn(dto);

    CommentLikeDto result = commentLikeService.like(userId, commentId);

    assertEquals(dto.getCommentId(), result.getCommentId());
    assertEquals(dto.getLikedBy(), result.getLikedBy());
    assertEquals(1, result.getCommentLikeCount());
    verify(commentLikeRepository).save(any(CommentLike.class));
    verify(commentLikeMLogService).addCommentLikeLog(userId, mockUser.getNickname(), like,
        mockComment);

  }

  @Test
  void 좋아요_취소_성공() {
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

    commentLikeService.unlike(userId, commentId);

    verify(commentLikeRepository).deleteByUserIdAndCommentId(userId, commentId);
    assertEquals(0, mockComment.getLikeCount());
    verify(commentLikeMLogService).deleteCommentLikeLog(userId, commentId);

  }
}
