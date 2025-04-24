package com.example.monewteam08.repository;

import com.example.monewteam08.entity.CommentLikeLog;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeLogRepository extends JpaRepository<CommentLikeLog, UUID> {

  @Query("select count(c) from CommentLikeLog c where c.activityLog.user.id = :userId")
  int countCommentLikeLogByUserId(@Param("userId") UUID userId);

  @Query("select c from CommentLikeLog c where c.activityLog.user.id = :userId order by c.createdAt asc")
  List<CommentLikeLog> findOldestLogs(@Param("userId") UUID userId, Pageable pageable);

}
