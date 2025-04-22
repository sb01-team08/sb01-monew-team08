package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Comment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CommentRepositoryCustom {

  Optional<Comment> findById(UUID commentId);

  boolean existsById(UUID id);

  void deleteById(UUID id);
}
