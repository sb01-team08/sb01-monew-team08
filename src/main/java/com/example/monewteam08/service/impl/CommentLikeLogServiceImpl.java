package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.CommentLikeLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.exception.useractivitylog.UserActicityLogNotFoundException;
import com.example.monewteam08.mapper.CommentLikeLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentLikeLogRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentLikeLogService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeLogServiceImpl implements CommentLikeLogService {

  private final CommentLikeLogRepository commentLikeLogRepository;
  private final UserActivityLogRepository userActivityLogRepository;
  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;

  private final CommentLikeLogMapper commentLikeLogMapper;

  private static final int LIMIT_SIZE = 10;
  private final UserRepository userRepository;

  // 이미 시작된 트랜잭션 내에서만 실행되도록
  @Transactional(propagation = Propagation.MANDATORY)
  @Override
  public void addCommentLikeLog(UUID userId, CommentLike commentLike, Comment comment) {
    log.debug("댓글 좋아요 로그 추가 요청: userId={}", userId);
    UserActivityLog userActivityLog = userActivityLogRepository.findByUserId(userId).orElseThrow(
        () -> new UserActicityLogNotFoundException(userId));
    Article article = articleRepository.findById(comment.getArticleId())
        .orElseThrow(() -> new ArticleNotFoundException(comment.getArticleId()));
    User commentUser = userRepository.findById(comment.getUserId())
        .orElseThrow(() -> new UserNotFoundException(comment.getUserId()));
    CommentLikeLog commentLikeLog = commentLikeLogMapper.toEntity(userActivityLog, comment,
        article, commentUser);

    commentLikeLogRepository.save(commentLikeLog);
    log.info("댓글 좋아요 로그 생성 완료");
  }

  // 이미 시작된 트랜잭션 내에서만 실행되도록
  @Transactional(propagation = Propagation.MANDATORY)
  @Override
  public void removeCommentLikeLogOnCancel(UUID userId, UUID commentId) {
    log.debug("댓글 좋아요 로그 삭제 요청: userId={}, commentId={}", userId, commentId);
    commentLikeLogRepository.deleteCommentLikeLogByCommentIdAndUserId(userId, commentId);
    log.info("댓글 좋아요 로그 삭제 완료");
  }

  @Override
  public List<CommentLikeLogResponse> getCommentLikeLogs(UserActivityLog userActivityLog) {
    log.debug("댓글 좋아요 로그 조회 요청: userId={}", userActivityLog.getUser().getId());
    List<CommentLikeLog> commentLikeLogs = commentLikeLogRepository.getCommentLikeLogsByActivityLogOrderByCreatedAtDesc(
        userActivityLog, PageRequest.of(0, LIMIT_SIZE));

    List<CommentLikeLogResponse> responseList = commentLikeLogs.stream()
        .map(commentLikeLogMapper::toResponse)
        .toList();

    log.info("댓글 좋아요 로그 조회 완료");
    return responseList;
  }
}
