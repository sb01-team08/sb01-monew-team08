package com.example.monewteam08.repository;

import static com.example.monewteam08.entity.QNotification.notification;

import com.example.monewteam08.entity.Notification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Notification> findUnreadByCursor(UUID userId, LocalDateTime cursor,
      LocalDateTime after,
      int limit) {
    return queryFactory
        .selectFrom(notification)
        .where(
            notification.userId.eq(userId),
            notification.isConfirmed.isFalse(),
            cursorCondition(cursor, after)
        )
        .orderBy(notification.createdAt.desc(), notification.id.desc())
        .limit(limit)
        .fetch();
  }

  private BooleanExpression cursorCondition(LocalDateTime cursor,
      LocalDateTime after) {
    if (cursor != null && after != null) {
      return notification.createdAt.lt(cursor)
          .or(notification.createdAt.eq(cursor).and(notification.createdAt.lt(after)));
    } else if (cursor != null) {
      return notification.createdAt.lt(cursor);
    } else {
      return null;
    }
  }
}
