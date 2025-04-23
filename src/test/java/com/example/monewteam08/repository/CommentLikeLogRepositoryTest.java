package com.example.monewteam08.repository;

import com.example.monewteam08.config.JacConfig;
import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.CommentLikeLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import({JacConfig.class, QuerydslConfig.class})
class CommentLikeLogRepositoryTest {

  @Autowired
  private CommentLikeLogRepository commentLikeLogRepository;
  @Autowired
  private UserActivityLogRepository userActivityLogRepository;
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("같은 ActivityLog(같은 사용자) CommentLikeLogs의 개수를 센다")
  void countCommentLikeLogByActivityLog() {
    // given
    User user = userRepository.save(new User("test@example.com", "tester", "tester1234!"));
    UserActivityLog activityLog = userActivityLogRepository.save(new UserActivityLog(user));

    for (int i = 0; i < 5; i++) {
      CommentLikeLog log = CommentLikeLog.builder()
          .activityLog(activityLog)
          .commentId(UUID.randomUUID())
          .articleId(UUID.randomUUID())
          .articleTitle("title" + i)
          .commentContent("content" + i)
          .commentCreatedAt(LocalDateTime.now().minusDays(i))
          .commentUserId(user.getId())
          .commentUserNickname(user.getNickname())
          .build();
      commentLikeLogRepository.save(log);
    }

    // when
    int count = commentLikeLogRepository.countCommentLikeLogByUserId(user.getId());

    // then
    Assertions.assertThat(count).isEqualTo(5);
  }

  @Test
  @DisplayName("오래된 n개의 로그를 가져온다.")
  void findOldestLogs() {
    // given
    User user = userRepository.save(new User("test@example.com", "tester", "secure123!"));
    UserActivityLog activityLog = userActivityLogRepository.save(new UserActivityLog(user));

    List<CommentLikeLog> logs = IntStream.range(0, 12)
        .mapToObj(i -> CommentLikeLog.builder()
            .activityLog(activityLog)
            .commentId(UUID.randomUUID())
            .articleId(UUID.randomUUID())
            .articleTitle("Article " + i)
            .commentContent("Comment " + i)
            .commentCreatedAt(LocalDateTime.now().minusDays(5 - i)) // 오래된 순
            .commentUserId(user.getId())
            .commentUserNickname(user.getNickname())
            .build())
        .toList();

    commentLikeLogRepository.saveAll(logs);

    // when
    List<CommentLikeLog> oldLogs = commentLikeLogRepository.findOldestLogs(user.getId(),
        PageRequest.of(0, 2));

    // then
    Assertions.assertThat(oldLogs).hasSize(2);
    Assertions.assertThat(oldLogs.get(0).getCommentContent()).isEqualTo("Comment 0");
    Assertions.assertThat(oldLogs.get(1).getCommentContent()).isEqualTo("Comment 1");
  }

}