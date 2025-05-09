package com.example.monewteam08.repository;

import com.example.monewteam08.entity.CommentLikeLog;
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
public interface CommentLikeLogRepository extends JpaRepository<CommentLikeLog, UUID> {

  @Query("select count(c) from CommentLikeLog c where c.activityLog.user.id = :userId")
  int countCommentLikeLogByUserId(@Param("userId") UUID userId);

  @Modifying
  @Query("delete from CommentLikeLog c where c.comment.id = :commentId and c.activityLog.user.id = :userId")
  void deleteCommentLikeLogByCommentIdAndUserId(@Param("userId") UUID userId,
      @Param("commentId") UUID commentId);

  @Query("select cll from CommentLikeLog cll join fetch cll.comment join fetch cll.commentUser where cll.activityLog = :activityLog order by cll.createdAt desc")
  List<CommentLikeLog> getCommentLikeLogsByActivityLogOrderByCreatedAtDesc(
      @Param("activityLog") UserActivityLog activityLog, Pageable pageable);
}
