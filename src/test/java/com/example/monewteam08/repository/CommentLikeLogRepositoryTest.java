package com.example.monewteam08.repository;

import com.example.monewteam08.config.JacConfig;
import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLikeLog;
import com.example.monewteam08.entity.Interest;
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
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ArticleRepository articleRepository;
  @Autowired
  private InterestRepository interestRepository;

  @Test
  @DisplayName("좋아요 취소 시 댓글 좋아요 로그를 userId와 commnetId를 받아 삭제한다.")
  void deleteCommentLikeLogByUserIdAndCommentId() {
    // given
    User user = userRepository.save(new User("test@example.com", "tester", "tester1234!"));
    Interest interest = interestRepository.save(new Interest("IT", List.of("AI")));
    Article article = articleRepository.save(new Article(
        "source", "title", "summary", "https://source.url", LocalDateTime.now(), interest.getId()
    ));
    Comment comment = commentRepository.save(
        new Comment(article.getId(), user.getId(), "content"));
    UserActivityLog activityLog = userActivityLogRepository.save(new UserActivityLog(user));
    CommentLikeLog log = CommentLikeLog.builder()
        .activityLog(activityLog)
        .comment(comment)
        .articleId(UUID.randomUUID())
        .articleTitle("title")
        .commentContent("content")
        .commentCreatedAt(LocalDateTime.now())
        .commentUser(user)
        .build();
    commentLikeLogRepository.save(log);

    // when
    commentLikeLogRepository.deleteCommentLikeLogByCommentIdAndUserId(user.getId(),
        comment.getId());

    // then
    int count = commentLikeLogRepository.countCommentLikeLogByUserId(user.getId());
    Assertions.assertThat(count).isZero();
  }

  @Test
  @DisplayName("댓글 좋아요 로그를 가장 최신 순으로 10개 가져온다.")
  void getCommentLikeLogsByUserActivityLogDesc() {
    // given
    int limit = 10;
    User user = userRepository.save(new User("test@example.com", "tester", "tester1234!"));
    Interest interest = interestRepository.save(new Interest("IT", List.of("AI")));
    Article article = articleRepository.save(new Article(
        "source", "title", "summary", "https://source.url", LocalDateTime.now(), interest.getId()
    ));
    UserActivityLog activityLog = userActivityLogRepository.save(new UserActivityLog(user));
    List<CommentLikeLog> logs = IntStream.range(0, 12)
        .mapToObj(i -> {
          Comment comment = commentRepository.save(
              new Comment(article.getId(), user.getId(), "content"));

          return CommentLikeLog.builder()
              .activityLog(activityLog)
              .comment(comment)
              .articleId(UUID.randomUUID())
              .articleTitle("Article " + i)
              .commentContent("Comment " + i)
              .commentCreatedAt(LocalDateTime.now().minusDays(5 - i)) // 오래된 순
              .commentUser(user)
              .build();
        })
        .toList();

    commentLikeLogRepository.saveAll(logs);

    // when
    List<CommentLikeLog> result = commentLikeLogRepository.getCommentLikeLogsByActivityLogOrderByCreatedAtDesc(
        activityLog,
        PageRequest.of(0, limit));

    // then
    Assertions.assertThat(result).hasSize(10);
    Assertions.assertThat(result.get(0).getCommentContent()).isEqualTo("Comment 11");
  }


}