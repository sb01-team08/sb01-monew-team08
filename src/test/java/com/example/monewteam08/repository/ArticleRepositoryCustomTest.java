package com.example.monewteam08.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.monewteam08.config.QuerydslConfig;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.QArticle;
import com.example.monewteam08.entity.QComment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class ArticleRepositoryCustomTest {

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private JPAQueryFactory queryFactory;

  private Article article1;
  private Article article2;
  private Article article3;

  @BeforeEach
  void setUp() {
    LocalDateTime now = LocalDateTime.of(2025, 4, 28, 0, 0, 0);

    article1 = articleRepository.save(
        new Article("HANKYUNG", "경제 뉴스", "주식 시장 상승", "http://a.com", now.minusDays(2), null));

    article2 = articleRepository.save(
        new Article("NAVER", "기술 업데이트", "새 스마트폰 출시", "http://b.com", now.minusDays(1), null));

    article3 = articleRepository.save(
        new Article("NAVER", "스포츠 하이라이트", "축구 결승전", "http://c.com", now, null));
  }

  @Test
  void 전체_조회_성공() {
    // when
    List<Article> articles = articleRepository.findAllByCursor(null, null, null, null, null,
        "publishDate", "DESC", null, null, 10);

    // then
    assertThat(articles).hasSize(3);
  }

  @Test
  void 키워드_검색_성공() {
    // when
    List<Article> articles = articleRepository.findAllByCursor("경제", null, null, null, null,
        "publishDate", "DESC", null, null, 10);

    // then
    assertThat(articles).hasSize(1);
    assertThat(articles.get(0).getTitle()).contains("경제");
  }

  @Test
  void 소스_검색_성공() {
    // when
    List<Article> articles = articleRepository.findAllByCursor(null, null, List.of("HANKYUNG"),
        null, null, "publishDate", "DESC", null, null, 10);

    // then
    assertThat(articles).hasSize(1);
    assertThat(articles.get(0).getSource()).isEqualTo("HANKYUNG");
  }

  @Test
  void 발행일_범위_검색_성공() {
    // given
    LocalDateTime from = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
    LocalDateTime to = LocalDateTime.now();

    // when
    List<Article> articles = articleRepository.findAllByCursor(null, null, null, from, to,
        "publishDate", "DESC", null, null, 10);

    // then
    assertThat(articles).hasSize(2);
  }

  @Test
  void 전체_카운트_성공() {
    // when
    long count = articleRepository.countAllByCondition(null, null, null, null, null);

    // then
    assertThat(count).isEqualTo(3);
  }

  @Test
  void 키워드_카운트_성공() {
    // when
    long count = articleRepository.countAllByCondition("기술", null, null, null, null);

    // then
    assertThat(count).isEqualTo(1);
  }

  @Test
  void 소스_카운트_성공() {
    // when
    long count = articleRepository.countAllByCondition(null, null, List.of("NAVER"), null, null);

    // then
    assertThat(count).isEqualTo(2);
  }

  @Test
  void 발행일_범위_카운트_성공() {
    // given
    LocalDateTime from = LocalDateTime.now().minusDays(3);
    LocalDateTime to = LocalDateTime.now();

    // when
    long count = articleRepository.countAllByCondition(null, null, null, from, to);

    // then
    assertThat(count).isEqualTo(3);
  }

  @Test
  void 댓글수_내림차순_정렬_성공() {
    // given
    QArticle article = QArticle.article;
    QComment comment = QComment.comment;
    String orderBy = "commentCount";
    String direction = "DESC";
    String cursor = "5";
    LocalDateTime after = LocalDateTime.now();

    // when
    BooleanExpression condition = new ArticleRepositoryCustomImpl(
        queryFactory).buildCursorCondition(article, comment, orderBy, direction, cursor, after);

    // then
    assertThat(condition).isNotNull();
  }

  @Test
  void 댓글수_오름차순_정렬_성공() {
    // given
    QArticle article = QArticle.article;
    QComment comment = QComment.comment;
    String orderBy = "commentCount";
    String direction = "ASC";
    String cursor = "3"; // 댓글 3개
    LocalDateTime after = LocalDateTime.now();

    // when
    BooleanExpression condition = new ArticleRepositoryCustomImpl(
        queryFactory).buildCursorCondition(article, comment, orderBy, direction, cursor, after);

    // then
    assertThat(condition).isNotNull();
  }

  @Test
  void 조회수_내림차순_정렬_성공() {
    // given
    QArticle article = QArticle.article;
    QComment comment = QComment.comment;
    String orderBy = "viewCount";
    String direction = "DESC";
    String cursor = "100"; // 조회수 100
    LocalDateTime after = LocalDateTime.now();

    // when
    BooleanExpression condition = new ArticleRepositoryCustomImpl(
        queryFactory).buildCursorCondition(article, comment, orderBy, direction, cursor, after);

    // then
    assertThat(condition).isNotNull();
  }

  @Test
  void 조회수_오름차순_정렬_성공() {
    // given
    QArticle article = QArticle.article;
    QComment comment = QComment.comment;
    String orderBy = "viewCount";
    String direction = "ASC";
    String cursor = "200"; // 조회수 200
    LocalDateTime after = LocalDateTime.now();

    // when
    BooleanExpression condition = new ArticleRepositoryCustomImpl(
        queryFactory).buildCursorCondition(article, comment, orderBy, direction, cursor, after);

    // then
    assertThat(condition).isNotNull();
  }

  @Test
  void 발행일_내림차순_정렬_성공() {
    // given
    QArticle article = QArticle.article;
    QComment comment = QComment.comment;
    String cursor = LocalDateTime.now().toString(); // 현재 시간
    LocalDateTime after = LocalDateTime.now().minusHours(1);

    // when
    BooleanExpression condition = new ArticleRepositoryCustomImpl(
        queryFactory).buildCursorCondition(article, comment, null, null, cursor, after);

    // then
    assertThat(condition).isNotNull();
  }

  @Test
  void 발행일_오름차순_정렬_성공() {
    // given
    QArticle article = QArticle.article;
    QComment comment = QComment.comment;
    String orderBy = "publishDate";
    String direction = "ASC";
    String cursor = LocalDateTime.now().toString(); // 현재 시간
    LocalDateTime after = LocalDateTime.now().plusHours(1);

    // when
    BooleanExpression condition = new ArticleRepositoryCustomImpl(
        queryFactory).buildCursorCondition(article, comment, orderBy, direction, cursor, after);

    // then
    assertThat(condition).isNotNull();
  }

  @Test
  void 커서_null값일때_조건_null_반환() {
    // given
    QArticle article = QArticle.article;
    QComment comment = QComment.comment;

    // when
    BooleanExpression condition = new ArticleRepositoryCustomImpl(
        queryFactory).buildCursorCondition(article, comment, "publishDate", "DESC", null, null);

    // then
    assertThat(condition).isNull();
  }

  @Test
  void 댓글수_기준_정렬_findAllByCursor_성공() {
    // given
    Article articleWithComments = articleRepository.save(
        new Article("NAVER", "News with comments", "Many comments", "http://naver.com/comments",
            LocalDateTime.now().minusDays(1), null));

    Article articleNoComments = articleRepository.save(
        new Article("NAVER", "News no comments", "No comments", "http://naver.com/nocomments",
            LocalDateTime.now(), null));

    // when
    List<Article> articles = articleRepository.findAllByCursor(null, null, null, null, null,
        "commentCount", "DESC", null, null, 10);

    // then
    assertThat(articles).isNotEmpty();
  }

  @Test
  void 조회수_기준_정렬_성공() {
    // given
    Article article1 = articleRepository.save(
        new Article("NAVER", "조회수 많은 뉴스", "조회수 100", "http://naver.com/view1",
            LocalDateTime.now().minusDays(1), null));
    article1.addViewCount();
    article1.addViewCount();
    article1.addViewCount();
    articleRepository.save(article1);

    Article article2 = articleRepository.save(
        new Article("NAVER", "조회수 적은 뉴스", "조회수 0", "http://naver.com/view2", LocalDateTime.now(),
            null));

    // when
    List<Article> articles = articleRepository.findAllByCursor(null, null, null, null, null,
        "viewCount", "DESC", null, null, 10);

    // then
    assertThat(articles.get(0).getViewCount()).isGreaterThanOrEqualTo(
        articles.get(1).getViewCount());
  }

  @Test
  void 디폴트_정렬_성공() {
    // when
    List<Article> articles = articleRepository.findAllByCursor(null, null, null, null, null, null,
        null, null, null, 10);

    // then
    assertThat(articles).isSortedAccordingTo((a1, a2) -> {
      int result = a2.getPublishDate().compareTo(a1.getPublishDate());
      if (result != 0) {
        return result;
      }
      return a2.getId().compareTo(a1.getId());
    });
  }

}
