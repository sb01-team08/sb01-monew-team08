package com.example.monewteam08.exception.user;

import com.example.monewteam08.exception.ErrorCode;
import com.example.monewteam08.exception.MonewException;

import java.util.Map;

public abstract class UserException extends MonewException {
    protected UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected UserException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
