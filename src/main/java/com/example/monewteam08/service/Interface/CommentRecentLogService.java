package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.UserActivityLog;
import java.util.List;
import java.util.UUID;

public interface CommentRecentLogService {

  // 댓글 추가 시 로그 함께 추가
  void addCommentRecentLog(UUID userId, Comment comment);

  // 댓글 삭제 시 로그 삭제
  void removeCommentRecentLog(UUID userId, UUID commentId);

  // 로그 조회 (최대 10개, 최신)
  List<CommentRecentLogResponse> getCommentRecentLogs(UserActivityLog userActivityLog);
}
