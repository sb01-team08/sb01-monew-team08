package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.CommentLikeMLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.CommentLikeMLogMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentLikeMLogService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeMLogServiceImpl implements CommentLikeMLogService {

  private final UserActivityMongoRepository userActivityMongoRepository;
  private final CommentLikeMLogMapper commentLikeMLogMapper;
  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  @Override
  public void addCommentLikeLog(UUID userId, String userNickname, CommentLike commentLike,
      Comment comment) {
    UserActivity userActivity = userActivityMongoRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    Article article = articleRepository.findById(comment.getArticleId())
        .orElseThrow(() -> new ArticleNotFoundException(comment.getArticleId()));

    CommentLikeMLog commentLikeMLog = commentLikeMLogMapper.toEntity(commentLike, comment, article,
        userId);

    List<CommentLikeMLog> commentLikes = userActivity.getCommentLikes();
    commentLikes.add(0, commentLikeMLog);

    if (commentLikes.size() > 10) {
      commentLikes = commentLikes.subList(0, 10);
    }

    userActivity.updateCommentLikes(commentLikes);
    userActivityMongoRepository.save(userActivity);
    log.info("댓글 좋아요 로그 추가 완료: userId={}, commentId={}", userId, comment.getId());
  }

  @Override
  public void deleteCommentLikeLog(UUID userId, UUID commentId) {
    UserActivity userActivity = userActivityMongoRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    List<CommentLikeMLog> commentLikes = userActivity.getCommentLikes();
    boolean removed = commentLikes.removeIf(commentLikeLog ->
        commentLikeLog.getCommentId().equals(commentId) && commentLikeLog.getCommentUserId()
            .equals(userId)
    );

    if (removed) {
      userActivityMongoRepository.save(userActivity);
      log.info("댓글 좋아요 로그 삭제 완료: userId={}, commentId={}", userId, commentId);
    }

  }

  @Override
  public List<CommentLikeLogResponse> getCommentLikeLogs(List<CommentLikeMLog> commentLikeMLogs) {
    // commentId / userId 수집
    Set<UUID> commentIds = commentLikeMLogs.stream()
        .map(CommentLikeMLog::getCommentId)
        .collect(Collectors.toSet());

    Set<UUID> userIds = commentLikeMLogs.stream()
        .map(CommentLikeMLog::getCommentUserId)
        .collect(Collectors.toSet());

    Map<UUID, Comment> commentMap = commentRepository.findAllById(commentIds).stream()
        .collect(Collectors.toMap(Comment::getId, Function.identity()));

    Map<UUID, String> nicknameMap = userRepository.findAllById(userIds).stream()
        .collect(Collectors.toMap(User::getId, User::getNickname));

    return commentLikeMLogs.stream().map(commentLikeMLog -> {
      Comment comment = commentMap.get(commentLikeMLog.getCommentId());
      int commentLikeCount = comment != null ? comment.getLikeCount() : 0;
      String content = comment != null ? comment.getContent() : "";
      String nickname = nicknameMap.getOrDefault(commentLikeMLog.getCommentUserId(), "알 수 없음");
      return commentLikeMLogMapper.toResponse(commentLikeMLog, commentLikeCount, nickname, content);
    }).collect(Collectors.toList());
  }
}
