package com.example.monewteam08.exception.comment;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;
import java.time.Instant;
import java.util.Map;

public class CommentException extends MonewException {

    public CommentException(ErrorCode errorCode,
            Instant timestamp) {
        super(errorCode, timestamp);
    }

    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommentException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
