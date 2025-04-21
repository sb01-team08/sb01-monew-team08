package com.example.monewteam08.service.Interface;

import dto.request.comment.CommentRegisterRequest;
import dto.request.comment.CommentUpdateRequest;
import dto.response.comment.CommentDto;
import java.util.UUID;

public interface CommentService {

    CommentDto create(CommentRegisterRequest request);

    CommentDto update(UUID id, CommentUpdateRequest request);

    void delete(UUID id, UUID userId);

    void delete_Hard(UUID id);
}
