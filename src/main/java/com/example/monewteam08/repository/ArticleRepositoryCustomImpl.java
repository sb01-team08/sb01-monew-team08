package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.QArticle;
import com.example.monewteam08.entity.QComment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Article> findAllByCursor(
      String keyword, UUID interestId, List<String> sourceIn,
      LocalDateTime publishDateFrom, LocalDateTime publishDateTo,
      String orderBy, String direction, String cursor, LocalDateTime after, int limit) {

    QArticle article = QArticle.article;
    QComment comment = QComment.comment;
    BooleanBuilder where = new BooleanBuilder();

    where.and(article.isActive.isTrue());

    if (keyword != null && !keyword.isBlank()) {
      where.and(article.title.containsIgnoreCase(keyword)
          .or(article.summary.containsIgnoreCase(keyword)));
    }
    if (interestId != null) {
      where.and(article.interestId.eq(interestId));
    }
    if (sourceIn != null && !sourceIn.isEmpty()) {
      where.and(article.source.in(sourceIn));
    }
    if (publishDateFrom != null && publishDateTo != null) {
      where.and(article.publishDate.between(publishDateFrom, publishDateTo));
    } else if (publishDateFrom != null) {
      where.and(article.publishDate.goe(publishDateFrom));
    } else if (publishDateTo != null) {
      where.and(article.publishDate.loe(publishDateTo));
    }

    BooleanExpression cursorCondition = buildCursorCondition(article, comment, orderBy, direction,
        cursor, after);
    if (cursorCondition != null) {
      where.and(cursorCondition);
    }

    JPAQuery<Article> query = queryFactory.selectFrom(article);

    List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(article, comment, orderBy,
        direction);

    if ("commentCount".equalsIgnoreCase(orderBy)) {
      query.leftJoin(article.comments, comment)
          .on(comment.isActive.isTrue())
          .groupBy(article.id);
    }

    return query
        .where(where)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .limit(limit + 1)
        .fetch();
  }

  @Override
  public long countAllByCondition(
      String keyword,
      UUID interestId,
      List<String> sourceIn,
      LocalDateTime publishDateFrom,
      LocalDateTime publishDateTo
  ) {
    QArticle article = QArticle.article;
    BooleanBuilder where = new BooleanBuilder();

    where.and(article.isActive.isTrue());

    if (keyword != null && !keyword.isBlank()) {
      where.and(article.title.containsIgnoreCase(keyword)
          .or(article.summary.containsIgnoreCase(keyword)));
    }

    if (interestId != null) {
      where.and(article.interestId.eq(interestId));
    }

    if (sourceIn != null && !sourceIn.isEmpty()) {
      where.and(article.source.in(sourceIn));
    }

    if (publishDateFrom != null && publishDateTo != null) {
      where.and(article.publishDate.between(publishDateFrom, publishDateTo));
    } else if (publishDateFrom != null) {
      where.and(article.publishDate.goe(publishDateFrom));
    } else if (publishDateTo != null) {
      where.and(article.publishDate.loe(publishDateTo));
    }

    return queryFactory
        .select(article.count())
        .from(article)
        .where(where)
        .fetchOne();
  }

  private List<OrderSpecifier<?>> getOrderSpecifiers(QArticle article, QComment comment,
      String orderBy, String direction) {

    Order order = "ASC".equalsIgnoreCase(direction) ? Order.ASC : Order.DESC;

    if ("commentCount".equalsIgnoreCase(orderBy)) {
      return List.of(
          new OrderSpecifier<>(order, comment.count()),
          new OrderSpecifier<>(order, article.id)
      );
    } else if ("viewCount".equalsIgnoreCase(orderBy)) {
      return List.of(
          new OrderSpecifier<>(order, article.viewCount),
          new OrderSpecifier<>(order, article.id)
      );
    } else { // publishDate or default
      return List.of(
          new OrderSpecifier<>(order, article.publishDate),
          new OrderSpecifier<>(order, article.id)
      );
    }
  }

  protected BooleanExpression buildCursorCondition(QArticle article, QComment comment,
      String orderBy, String direction, String cursor, LocalDateTime after) {

    if (cursor == null || after == null) {
      return null;
    }

    if ("commentCount".equalsIgnoreCase(orderBy)) {
      long commentCount = Long.parseLong(cursor);
      return ("DESC".equalsIgnoreCase(direction)) ?
          comment.count().lt(commentCount)
              .or(comment.count().eq(commentCount).and(article.publishDate.lt(after))) :
          comment.count().gt(commentCount)
              .or(comment.count().eq(commentCount).and(article.publishDate.gt(after)));
    } else if ("viewCount".equalsIgnoreCase(orderBy)) {
      long viewCount = Long.parseLong(cursor);
      return ("DESC".equalsIgnoreCase(direction)) ?
          article.viewCount.lt(viewCount)
              .or(article.viewCount.eq(viewCount).and(article.publishDate.lt(after))) :
          article.viewCount.gt(viewCount)
              .or(article.viewCount.eq(viewCount).and(article.publishDate.gt(after)));
    } else {
      LocalDateTime dateCursor = LocalDateTime.parse(cursor);
      return ("ASC".equalsIgnoreCase(direction)) ?
          article.publishDate.gt(dateCursor)
              .or(article.publishDate.eq(dateCursor).and(article.publishDate.lt(after))) :
          article.publishDate.lt(dateCursor)
              .or(article.publishDate.eq(dateCursor).and(article.publishDate.gt(after)));
    }
  }
}
