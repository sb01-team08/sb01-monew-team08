package com.example.monewteam08.common;

import com.example.monewteam08.exception.ExceptionDto;
import com.example.monewteam08.exception.MonewException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.lang.Nullable;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomApiResponse<T>(
    @JsonIgnore
    HttpStatus httpStatus,
    boolean success,
    @Nullable T data,
    @Nullable ExceptionDto error
) {

  public static <T> CustomApiResponse<T> ok(@Nullable final T data) {
    return new CustomApiResponse<>(HttpStatus.OK, true, data, null);
  }

  public static <T> CustomApiResponse<T> create(@Nullable final T data) {
    return new CustomApiResponse<>(HttpStatus.CREATED, true, data, null);
  }

  public static <T> CustomApiResponse<T> delete() {
    return new CustomApiResponse<>(HttpStatus.NO_CONTENT, true, null, null);
  }

  public static <T> CustomApiResponse<T> fail(@Nullable final MonewException e) {
    return new CustomApiResponse<>(e.getErrorCode().getStatus(), false, null, ExceptionDto.of(e));
  }

  public static <T> CustomApiResponse<T> fail(final ExceptionDto e) {
    return new CustomApiResponse<>(e.getHttpCode(), false, null, e);
  }

}
