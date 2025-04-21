package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.request.comment.CommentRegisterRequest;
import com.example.monewteam08.dto.request.comment.CommentUpdateRequest;
import com.example.monewteam08.dto.response.comment.CommentDto;
import java.util.UUID;

public interface CommentService {

    CommentDto create(CommentRegisterRequest request);

    CommentDto update(UUID id, CommentUpdateRequest request);

    void delete(UUID id, UUID userId);

    void delete_Hard(UUID id);
}
