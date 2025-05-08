package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.useractivitylog.CommentRecentLogResponse;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentMLog;
import com.example.monewteam08.entity.User;
import java.util.List;

public interface CommentMLogService {

  void addRecentComment(User user, Comment comment, String title);

  List<CommentRecentLogResponse> getCommentRecentLogs(List<CommentMLog> commentMLogs);

}
