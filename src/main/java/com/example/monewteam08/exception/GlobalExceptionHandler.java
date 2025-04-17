package com.example.monewteam08.exception;

import com.example.monewteam08.common.CustomApiResponse;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 존재하지 않는 요청에 대한 예의
  @ExceptionHandler(value = {NoHandlerFoundException.class,
      HttpRequestMethodNotSupportedException.class})
  public CustomApiResponse<?> handleNoPageFoundException(Exception e) {
    log.error("Invalid route or method: : {}", e.getMessage());
    return CustomApiResponse.fail(new MonewException(ErrorCode.METHOD_NOT_ALLOWED));
  }

  // Validation 예외
  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public CustomApiResponse<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();

    List<String> errors = bindingResult.getFieldErrors().stream()
        .map(error -> String.format("[field=%s, rejected=%s, message=%s]",
            error.getField(),
            error.getRejectedValue(),
            error.getDefaultMessage()))
        .toList();

    log.warn("Validation Failed: {}", errors);
    return CustomApiResponse.fail(
        ExceptionDto.of(e, ErrorCode.INVALID_INPUT_VALUE, Map.of("validationError", errors)));
  }


  // 커스텀 예외
  @ExceptionHandler(value = {MonewException.class})
  public CustomApiResponse<?> handleMonewException(MonewException e) {
    ExceptionDto eDto = ExceptionDto.of(e);
    log.warn("DiscodeitException caught - exceptionType: {} | detail: {}", eDto.getExceptionType(),
        eDto);
    return CustomApiResponse.fail(eDto);
  }

  // 기본 예외
  @ExceptionHandler(value = {Exception.class})
  public CustomApiResponse<?> handleException(Exception e) {
    log.error("Unhandled exception caught in GlobalExceptionHandler : {}", e.getMessage());
    return CustomApiResponse.fail(new MonewException(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
