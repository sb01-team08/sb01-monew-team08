package com.example.monewteam08.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "에러 응답 DTO")
public class ExceptionDto {

  @Schema(description = "에러 발생 시각", example = "2025-03-31T10:12:30Z")
  private final Instant timestamp;

  @Schema(description = "HTTP 상태 코드", example = "400")
  private final HttpStatus httpCode;

  @Schema(description = "에러 코드", example = "VALID0001")
  private final String code;

  @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
  private final String message;

  @Schema(description = "예외 발생 상황에 대한 추가 정보")
  private final Map<String, Object> details;

  @Schema(description = "예외 클래스 이름", example = "DiscodeitException")
  private final String exceptionType;

  public static ExceptionDto of(MonewException e) {
    return new ExceptionDto(
        e.getTimestamp(),
        e.getErrorCode().getStatus(),
        e.getErrorCode().getCode(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName()
    );
  }

  public static ExceptionDto of(Exception e, ErrorCode errorCode, Map<String, Object> details) {
    return new ExceptionDto(
        Instant.now(),
        errorCode.getStatus(),
        errorCode.getCode(),
        errorCode.getMessage(),
        details,
        e.getClass().getSimpleName()
    );
  }

}
