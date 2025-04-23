package com.example.monewteam08.dto.response.interest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PageResponse<T>(
    List<T> content,
    UUID nextCursor,
    LocalDateTime nextAfter,
    int size,
    int totalElements,
    boolean hasNext
) {

}
