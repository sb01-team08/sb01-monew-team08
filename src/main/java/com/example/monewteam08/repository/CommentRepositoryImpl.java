package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.QComment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Comment> findAllByCursor(UUID articleId, String orderBy, String direction,
      String cursor, String after, int limit) {

    QComment comment = QComment.comment;
    BooleanBuilder where = new BooleanBuilder();

    if (articleId != null) {
      where.and(comment.articleId.eq(articleId));
    }
    where.and(buildCursorCondition(comment, orderBy, direction, cursor, after));

    List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(comment, orderBy, direction);

    return queryFactory
        .selectFrom(comment)
        .where(where)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .limit(limit + 1)
        .fetch();
  }

  private List<OrderSpecifier<?>> getOrderSpecifiers(QComment comment, String orderBy,
      String direction) {
    Order order = "DESC".equalsIgnoreCase(direction) ? Order.DESC : Order.ASC;
    if ("likeCount".equals(orderBy)) {
      return List.of(
          new OrderSpecifier<>(order, comment.likeCount),
          new OrderSpecifier<>(order, comment.createdAt),
          new OrderSpecifier<>(order, comment.id));
    } else {
      return List.of(new OrderSpecifier<>(order, comment.createdAt),
          new OrderSpecifier<>(order, comment.id));
    }
  }

  private BooleanExpression buildCursorCondition(QComment comment,
      String orderBy, String direction, String cursor, String after) {

    if (cursor == null || after == null) {
      return null;
    }

    UUID cursorId = UUID.fromString(after);

    if ("likeCount".equals(orderBy)) {
      int likeCount = Integer.parseInt(cursor);

      if ("DESC".equalsIgnoreCase(direction)) {
        return comment.likeCount.lt(likeCount)
            .or(comment.likeCount.eq(likeCount)
                .and(comment.id.lt(cursorId)));
      } else {
        return comment.likeCount.gt(likeCount)
            .or(comment.likeCount.eq(likeCount)
                .and(comment.id.gt(cursorId)));
      }

    } else if ("createdAt".equals(orderBy)) {
      LocalDateTime createdCursor = LocalDateTime.parse(cursor);

      if ("DESC".equalsIgnoreCase(direction)) {
        return comment.createdAt.lt(createdCursor)
            .or(comment.createdAt.eq(createdCursor)
                .and(comment.id.lt(cursorId)));
      } else {
        return comment.createdAt.gt(createdCursor)
            .or(comment.createdAt.eq(createdCursor)
                .and(comment.id.gt(cursorId)));
      }
    }

    throw new IllegalArgumentException("유효하지 않는 정렬 기준입니다.");
  }
}

