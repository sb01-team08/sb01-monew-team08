package com.example.monewteam08.service;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import com.example.monewteam08.entity.Comment;
import com.example.monewteam08.exception.comment.CommentNotFoundException;
import com.example.monewteam08.exception.comment.UnauthorizedCommentAccessException;
import com.example.monewteam08.mapper.CommentMapper;
import com.example.monewteam08.repository.CommentRepository;
import com.example.monewteam08.service.Interface.CommentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  @Override
  @Transactional
  public CommentDto create(CommentRegisterRequest request) {
    log.info("댓글 생성 요청: articleId={}, userId={}, 내용={}", request.getArticleId(),
        request.getUserId(), request.getContent());
    UUID articleId = UUID.fromString(request.getArticleId());
    UUID userId = UUID.fromString(request.getUserId());

    //유저,기사 있는지 없는지 예외 체크 필요

    Comment comment = new Comment(articleId, userId, request.getContent());
    Comment save = commentRepository.save(comment);

    return commentMapper.toDto(save);
  }

  @Override
  @Transactional
  public CommentDto update(UUID id, CommentUpdateRequest request) {
    Comment comment = commentRepository.findById(id).orElseThrow(() -> {
      log.warn("수정 할 댓글 없음 : id={}", id);
      return new CommentNotFoundException();
    });

    comment.update(request.getContent());
    log.info("댓글 수정 완료");

    return commentMapper.toDto(comment);
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
}
