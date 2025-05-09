package com.example.monewteam08.repository;

import com.example.monewteam08.entity.CommentRecentLog;
import com.example.monewteam08.entity.UserActivityLog;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRecentLogRepository extends JpaRepository<CommentRecentLog, UUID> {

  @Modifying
  @Query("delete from CommentRecentLog c where c.comment.id = :commentId and c.activityLog.user.id = :userId")
  void deleteCommentRecentLogByCommentIdAndUserId(@Param("userId") UUID userId,
      @Param("commentId") UUID commentId);

  @Query("select crl from CommentRecentLog crl join fetch crl.comment join fetch crl.user where crl.activityLog = :activityLog order by crl.createdAt desc")
  List<CommentRecentLog> getCommentRecentLogsByActivityLogOrderByCreatedAtDesc(
      @Param("activityLog") UserActivityLog activityLog, Pageable pageable);
}
