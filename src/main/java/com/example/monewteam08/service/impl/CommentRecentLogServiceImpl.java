package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentRecentLog;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.mapper.CommentRecentLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRecentLogRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.service.Interface.CommentRecentLogService;
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
public class CommentRecentLogServiceImpl implements CommentRecentLogService {

  private final UserActivityLogRepository userActivityLogRepository;
  private final CommentRecentLogRepository commentRecentLogRepository;

  private final CommentRecentLogMapper commentRecentLogMapper;

  private static final int LIMIT_SIZE = 10;
  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;

  @Transactional(propagation = Propagation.MANDATORY)
  @Override
  public void addCommentRecentLog(UUID userId, Comment comment) {
    log.debug("최신 작성 댓글 로그 추가 요청: userId={}", userId);
    // todo: exception 필요
    UserActivityLog userActivityLog = userActivityLogRepository.findByUserId(userId).orElseThrow();
    Article article = articleRepository.findById(comment.getArticleId())
        .orElseThrow(() -> new ArticleNotFoundException(comment.getArticleId()));
    CommentRecentLog commentRecentLog = commentRecentLogMapper.toEntity(userActivityLog, comment,
        article.getTitle());
    commentRecentLogRepository.save(commentRecentLog);
    log.info("최신 작성 댓글 로그 생성 완료: userId={}, logId={}", userId, commentRecentLog.getId());
  }

  @Transactional(propagation = Propagation.MANDATORY)
  @Override
  public void removeCommentRecentLog(UUID userId, UUID commentId) {
    log.debug("최신 작성 댓글 로그 삭제 요청: userId={}, commentId={}", userId, commentId);
    commentRecentLogRepository.deleteCommentRecentLogByCommentIdAndUserId(userId, commentId);
    log.info("최신 작성 댓글 로그 삭제 완료: userId={}, commentId={}", userId, commentId);

  }

  @Override
  public List<CommentRecentLogResponse> getCommentRecentLogs(UserActivityLog userActivityLog) {
    log.debug("최신 작성 댓글 로그 조회 요청: userId={}", userActivityLog.getId());
    List<CommentRecentLog> commentRecentLogs = commentRecentLogRepository.getCommentLikeLogsByActivityLogOrderByCreatedAtDesc(
        userActivityLog, PageRequest.of(0, LIMIT_SIZE));

    List<CommentRecentLogResponse> commentRecentLogResponses = commentRecentLogs.stream()
        .map(commentRecentLog -> {
          Comment comment = commentRepository.findById(commentRecentLog.getCommentId()).orElseThrow(
              CommentNotFoundException::new);
          return commentRecentLogMapper.toResponse(commentRecentLog, comment.getLikeCount());
        })
        .toList();

    log.info("최신 작성 댓글 로그 조회 완료: userId={}, userActivityLogId={}", userActivityLog.getId(),
        userActivityLog.getId());
    return commentRecentLogResponses;
  }
}
