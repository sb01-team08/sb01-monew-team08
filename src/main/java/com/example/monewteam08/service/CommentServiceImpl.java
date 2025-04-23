package com.example.monewteam08.service;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.dto.response.comment.CursorPageResponseCommentDto;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.entity.CommentLike;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.exception.article.ArticleNotFoundException;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.exception.comment.UnauthorizedCommentAccessException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.CommentMapper;
import com.example.monewteam08.repository.ArticleRepository;
import com.example.monewteam08.repository.CommentLikeRepository;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.CommentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final CommentMapper commentMapper;
  private final CommentLikeRepository commentLikeRepository;

  @Override
  public CursorPageResponseCommentDto getCommentsByCursor(String articleId, String orderBy,
      String direction, String cursor, String after, int limit, String requestUserId) {
    UUID articleUuid = UUID.fromString(articleId);
    UUID requestUserUuid = UUID.fromString(requestUserId);

    UUID cursorUuid = null;
    if (cursor != null && after != null) {
      cursorUuid = UUID.fromString(cursor);
    }

    List<Comment> comments = commentRepository.findAllByCursor(
        articleUuid, orderBy, direction, cursor, after, limit
    );

    boolean hasNext = comments.size() > limit;
    if (hasNext) {
      comments.remove(limit);
    }

    //id
    Set<UUID> userIds = comments.stream().map(Comment::getUserId).collect(Collectors.toSet());

    Set<UUID> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toSet());

    //닉네임
    List<User> users = userRepository.findByIdIn(userIds);
    Map<UUID, String> nicknmaMap = users.stream()
        .collect(Collectors.toMap(User::getId, User::getNickname));

    //좋아요
    List<CommentLike> likes = commentLikeRepository.findAllByUserIdAndCommentIdIn(
        requestUserUuid, commentIds);
    Set<UUID> likecCommentIds = likes.stream().map(CommentLike::getCommentId)
        .collect(Collectors.toSet());

    List<CommentDto> dtoList = comments.stream()
        .map(comment -> CommentDto.builder()
            .id(comment.getId().toString())
            .articleId(comment.getArticleId().toString())
            .userId(comment.getUserId().toString())
            .userNickname(nicknmaMap.get(comment.getUserId()))
            .content(comment.getContent())
            .likeCount(comment.getLikeCount())
            .likedByMe(likecCommentIds.contains(comment.getId()))
            .createdAt(comment.getCreatedAt())
            .build()).toList();

    String nextCursor = null;
    String nextAfter = null;
    if (hasNext && !dtoList.isEmpty()) {
      CommentDto last = dtoList.get(dtoList.size() - 1);
      nextCursor = getCursorValue(last, orderBy);
      nextAfter = last.getId();
    }

    int totalElements = commentRepository.countByArticleId(articleUuid);

    CursorPageResponseCommentDto response = CursorPageResponseCommentDto.builder()
        .content(new ArrayList<>(dtoList))
        .nextCursor(nextCursor)
        .nextAfter(nextAfter)
        .size(limit)
        .totalElements(totalElements)
        .hasNext(hasNext)
        .build();

    return response;
  }

  @Override
  @Transactional
  public CommentDto create(CommentRegisterRequest request) {
    log.info("댓글 생성 요청: articleId={}, userId={}, 내용={}", request.getArticleId(),
        request.getUserId(), request.getContent());
    UUID articleId = UUID.fromString(request.getArticleId());
    UUID userId = UUID.fromString(request.getUserId());

    articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    Comment comment = new Comment(articleId, userId, request.getContent());
    Comment save = commentRepository.save(comment);

    return commentMapper.toDto(save, user.getNickname(), false);
  }

  @Override
  @Transactional
  public CommentDto update(UUID id, CommentUpdateRequest request, UUID userId) {
    Comment comment = commentRepository.findById(id).orElseThrow(() -> {
      log.warn("수정 할 댓글 없음 : id={}", id);
      return new CommentNotFoundException();
    });

    if (!comment.getUserId().equals(userId)) {
      throw new UnauthorizedCommentAccessException();
    }

    comment.update(request.getContent());
    log.info("댓글 수정 완료" );

    boolean likedByMe = commentLikeRepository.existsByUserIdAndCommentId(userId, comment.getId());

    String nickname = userRepository.findById(userId).map(User::getNickname).orElse("" );

    return commentMapper.toDto(comment, nickname, likedByMe);
  }

  //물리삭제
  @Override
  @Transactional
  public void delete(UUID id, UUID userId) {
    log.info("댓글 논리 삭제 요청 : id={}", id);

    Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);

    if (!comment.getUserId().equals(userId)) {
      throw new UnauthorizedCommentAccessException();
    }

    comment.deactivate();
    log.info("댓글 논리 삭제 완료 id={}", id);
  }

  //물리삭제
  @Override
  @Transactional
  public void delete_Hard(UUID id) {
    log.info("댓글 물리 삭제 요청 : id={}", id);

    Comment comment = commentRepository.findById(id)
        .orElseThrow(CommentNotFoundException::new);

    commentRepository.deleteById(id);
    log.info("댓글 물리 삭제 완료 id={}", id);
  }

  private String getCursorValue(CommentDto last, String orderBy) {
    return switch (orderBy) {
      case "likeCount" -> String.valueOf(last.getLikeCount());
      case "createdAt" -> String.valueOf(last.getCreatedAt());
      default -> throw new IllegalArgumentException("유효하지 않는 정렬 기준입니다." );
    };
  }
}
