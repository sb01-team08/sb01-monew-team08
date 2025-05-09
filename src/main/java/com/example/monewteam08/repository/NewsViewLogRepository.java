package com.example.monewteam08.repository;

import com.example.monewteam08.entity.NewsViewLog;
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
public interface NewsViewLogRepository extends JpaRepository<NewsViewLog, UUID> {

  @Modifying
  @Query("delete from NewsViewLog n where n.article.id = :articleId and n.activityLog.user.id = :userId")
  void deleteNewsViewLogByCommentIdAndUserId(@Param("userId") UUID userId,
      @Param("articleId") UUID articleId);

  @Query("select nvl from NewsViewLog nvl join fetch nvl.article where nvl.activityLog = :activityLog order by nvl.createdAt desc")
  List<NewsViewLog> getNewsViewLogsByActivityLogOrderByCreatedAtDesc(
      @Param("activityLog") UserActivityLog activityLog,
      Pageable pageable);

}
