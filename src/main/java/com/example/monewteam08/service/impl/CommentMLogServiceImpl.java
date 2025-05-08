package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentMLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivity;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.CommentMLogMapper;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserActivityMongoRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentMLogService;
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
public class CommentMLogServiceImpl implements CommentMLogService {

  private final UserActivityMongoRepository userActivityMongoRepository;
  private final CommentMLogMapper commentMLogMapper;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  @Override
  public void addRecentComment(User user, Comment comment,
      String title) {
    // todo: exception
    UserActivity userActivity = userActivityMongoRepository.findById(user.getId())
        .orElseThrow(() -> new UserNotFoundException(user.getId()));

    CommentMLog commentMLog = commentMLogMapper.toEntity(comment, title, user);

    List<CommentMLog> comments = userActivity.getComments();
    comments.add(0, commentMLog);

    if (comments.size() > 10) {
      comments = comments.subList(0, 10);
    }

    userActivity.updateComments(comments);
    userActivityMongoRepository.save(userActivity);
    log.info("댓글 활동 로그 추가 완료: userId={}, commentId={}", user.getId(), comment.getId());
  }

  @Override
  public List<CommentRecentLogResponse> getCommentRecentLogs(List<CommentMLog> commentMLogs) {
    // userId 모으기
    Set<UUID> userIds = commentMLogs.stream()
        .map(CommentMLog::getUserId)
        .collect(Collectors.toSet());

    Set<UUID> commentIds = commentMLogs.stream()
        .map(CommentMLog::getId)
        .collect(Collectors.toSet());

    Map<UUID, String> nicknameMap = userRepository.findAllById(userIds).stream()
        .collect(Collectors.toMap(User::getId, User::getNickname));

    Map<UUID, Comment> commentMap = commentRepository.findAllById(commentIds).stream()
        .collect(Collectors.toMap(Comment::getId, Function.identity()));

    return commentMLogs.stream().map(commentMLog -> {
      Comment comment = commentMap.get(commentMLog.getId());
      int likeCount = comment != null ? comment.getLikeCount() : 0;
      String content = comment != null ? comment.getContent() : "";
      String nickname = nicknameMap.getOrDefault(commentMLog.getUserId(), "알 수 없음");
      return commentMLogMapper.toResponse(commentMLog, likeCount, nickname, content);
    }).collect(Collectors.toList());
  }
}
