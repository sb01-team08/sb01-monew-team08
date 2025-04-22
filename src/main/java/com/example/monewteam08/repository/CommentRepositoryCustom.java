package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Comment;
import java.util.List;
import java.util.UUID;

public interface CommentRepositoryCustom {

  List<Comment> findAllByCursor(
      UUID articleId,
      String orderBy,
      String direction,
      String cursor,
      String after,
      int limit
  );
}
