package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.useractivitylog.CommentLikeLogResponse;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.CommentLikeMLog;
import java.util.List;
import java.util.UUID;

public interface CommentLikeMLogService {

  void addCommentLikeLog(UUID userId, String userNickname, CommentLike commentLike,
      Comment comment);

  void deleteCommentLikeLog(UUID userId, UUID commentId);

  List<CommentLikeLogResponse> getCommentLikeLogs(List<CommentLikeMLog> commentLikeMLogs);

}
