package com.example.monewteam08.service.Interface;

import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import java.util.List;
import java.util.UUID;

public interface CommentLikeLogService {

  // 댓글 좋아요 로그 테이블 추가
  void addCommentLikeLog(UUID userId, CommentLike commentLike, Comment comment);

  // 옛날 데이터 삭제 (10개 초과 시 삭제)
  void deleteExcessCommentLikeLogs(UUID userId);

  // 좋아요 취소 시 활동 로그 삭제
  void removeCommentLikeLogOnCancel(UUID userId, UUID commentId);

  // 좋아요 조회
  // todo: 자료형 정해줘야 함
  List<?> getCommentLikeLogs(UUID userId);

}
