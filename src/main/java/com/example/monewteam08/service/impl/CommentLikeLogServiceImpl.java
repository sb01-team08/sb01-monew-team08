package com.example.monewteam08.service.impl;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.CommentLikeLog;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.mapper.CommentLikeLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentLikeLogRepository;
import com.example.monewteam08.repository.CommentLikeRepository;
import com.example.monewteam08.repository.UserActivityLogRepository;
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

  private final CommentLikeLogMapper commentLikeLogMapper;
  private final CommentLikeRepository commentLikeRepository;

  private static final int LIMIT_SIZE = 10;

  // 이미 시작된 트랜잭션 내에서만 실행되도록
  @Transactional(propagation = Propagation.MANDATORY)
  @Override
  public void addCommentLikeLog(UUID userId, CommentLike commentLike, Comment comment) {
    // todo: exception 필요
    UserActivityLog userActivityLog = userActivityLogRepository.findByUserId(userId).orElseThrow();
    Article article = articleRepository.findById(comment.getArticleId()).orElseThrow();

    CommentLikeLog commentLikeLog = commentLikeLogMapper.toEntity(userActivityLog, comment,
        article);

    deleteExcessCommentLikeLogs(userId);

    commentLikeLogRepository.save(commentLikeLog);
  }

  @Override
  public void deleteExcessCommentLikeLogs(UUID userId) {
    int countLogs = commentLikeLogRepository.countCommentLikeLogByUserId(userId);
    if (countLogs > LIMIT_SIZE) {
      List<CommentLikeLog> oldLogs = commentLikeLogRepository.findOldestLogs(userId,
          PageRequest.of(0, countLogs - LIMIT_SIZE));
      commentLikeLogRepository.deleteAll(oldLogs);
    }
  }

  @Override
  public void removeCommentLikeLogOnCancel(UUID userId, UUID commentId) {

  }

  @Override
  public List<?> getCommentLikeLogs(UUID userId) {
    return List.of();
  }
}
