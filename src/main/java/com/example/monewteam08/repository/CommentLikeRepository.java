package com.example.monewteam08.repository;

import com.example.monewteam08.entity.CommentLike;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CommentLikeRepository extends CrudRepository<CommentLike, UUID> {

  void deleteByUserIdAndCommentId(UUID userId, UUID commentId);

  List<CommentLike> findAllByUserIdAndCommentIdIn(UUID userId, Set<UUID> commentIds);

  boolean existsByUserIdAndCommentId(UUID userId, UUID commentId);

  int countCommentLikeById(UUID id);
}
