package com.example.monewteam08.repository;

import com.example.monewteam08.entity.CommentLike;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CommentLikeRepository extends CrudRepository<CommentLike, UUID> {

    void deleteByUserIdAndCommentId(UUID userId, UUID commentId);
}
