package com.example.monewteam08.repository;

import com.example.monewteam08.config.JacConfig;
import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentRecentLog;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JacConfig.class, QuerydslConfig.class})
class CommentRecentLogRepositoryTest {

  @Autowired
  private CommentRecentLogRepository commentRecentLogRepository;
  @Autowired
  private UserActivityLogRepository userActivityLogRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ArticleRepository articleRepository;

  @Test
  @DisplayName("사용자 아이디와 댓글 아이디를 받아 로그 삭제를 진행한다.")
  void removeCommentRecentLog() {
    // given
    User user = userRepository.save(new User("test@example.com", "tester", "tester1234!"));
    Article article = articleRepository.save(
        new Article("CNN", "Tech Breakthrough", "Latest breakthrough in AI research",
            "https://cnn.com/tech/ai-breakthrough", LocalDateTime.now(), UUID.randomUUID()
        )
    );
    Comment comment = commentRepository.save(
        new Comment(article.getId(), user.getId(), "content"));
    UserActivityLog userActivityLog = userActivityLogRepository.save(new UserActivityLog(user));
    CommentRecentLog log = CommentRecentLog.builder()
        .activityLog(userActivityLog)
        .comment(comment)
        .articleId(UUID.randomUUID())
        .articleTitle("title")
        .user(user)
        .commentContent("content")
        .commentCreatedAt(LocalDateTime.now())
        .build();
    commentRecentLogRepository.save(log);
    UUID id = log.getId();

    // when
    commentRecentLogRepository.deleteCommentRecentLogByCommentIdAndUserId(user.getId(),
        comment.getId());

    // then
    boolean exist = commentRecentLogRepository.existsById(id);
    Assertions.assertThat(exist).isFalse();
  }

  @Test
  @DisplayName("대상 아이디가 없을 때")
  void failed_removeCommentRecentLog() {
    // given
    UUID commentId = UUID.randomUUID();
    User user = userRepository.save(new User("test@example.com", "tester", "tester1234!"));

    // when
    commentRecentLogRepository.deleteCommentRecentLogByCommentIdAndUserId(user.getId(), commentId);

    // then
    long exist = commentRecentLogRepository.count();
    Assertions.assertThat(exist).isZero();
  }


}