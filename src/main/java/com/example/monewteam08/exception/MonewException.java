package com.example.monewteam08.exception;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MonewException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Instant timestamp;
  private final Map<String, Object> details = new HashMap<>();

  public MonewException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.timestamp = Instant.now();
  }

  public MonewException(ErrorCode errorCode, Map<String, Object> details) {
    this.errorCode = errorCode;
    this.timestamp = Instant.now();
    this.details.putAll(details);
  }

  @Override
  public String getMessage() {
    return errorCode.getCode() + ": " + errorCode.getMessage();
  }
  
}
