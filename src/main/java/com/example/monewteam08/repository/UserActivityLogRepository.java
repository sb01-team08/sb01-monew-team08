package com.example.monewteam08.repository;

import com.example.monewteam08.entity.UserActivityLog;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

  @Query("select l from UserActivityLog l where l.user.id = :userId")
  Optional<UserActivityLog> findByUserId(@Param("userId") UUID userId);
}
