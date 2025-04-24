package com.example.monewteam08.service;

import com.example.monewteam08.dto.response.comment.CommentLikeDto;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.mapper.CommentLikeMapper;
import com.example.monewteam08.repository.CommentLikeRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentLikeLogService;
import com.example.monewteam08.service.Interface.CommentLikeService;
import com.example.monewteam08.service.Interface.NotificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

  private final CommentLikeRepository commentLikeRepository;
  private final CommentRepository commentRepository;
  private final CommentLikeMapper commentLikeMapper;
  private final NotificationService notificationService;
  private final UserRepository userRepository;
  private final CommentLikeLogService commentLikeLogService;

  @Transactional
  @Override
  public CommentLikeDto like(UUID userId, UUID commentId) {
    log.info("댓글 좋아요 요쳥: userId={},commentId={}", userId, commentId);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> {
          log.warn("댓글 좋아요 실패 - 존재하지 않는 댓글: commentId={}", commentId);
          return new CommentNotFoundException();
        });

    String nickname = userRepository.findById(userId).get().getNickname();

    CommentLike liked = commentLikeRepository.save(new CommentLike(userId, commentId));
    comment.increaseLikeCount();
    notificationService.createCommentLikeNotification(userId, commentId, nickname);
    commentLikeLogService.addCommentLikeLog(userId, liked, comment);  // 댓글 좋아요 로그 추가

    log.info("댓글 좋아요 성공");
    return commentLikeMapper.toDto(liked, comment, null);
  }

  @Transactional
  @Override
  public void unlike(UUID userId, UUID commentId) {
    log.info("댓글 좋아요 취소 요청 : userId={},commentId={}", userId, commentId);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(CommentNotFoundException::new);

    commentLikeRepository.deleteByUserIdAndCommentId(userId, commentId);
    comment.decreaseLikeCount();

    commentLikeLogService.removeCommentLikeLogOnCancel(userId, commentId); // 댓글 좋아요 로그 삭제

    log.info("댓글 취소 성공");
  }
}
