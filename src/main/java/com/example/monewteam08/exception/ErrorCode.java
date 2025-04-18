package com.example.monewteam08.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // COMMON
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON000", "서버 에러, 관리자에게 연락해주세요."),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON001", "잘못된 값을 입력하였습니다"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON002", "요청한 리소스를 찾을 수 없습니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON003", "허용되지 않은 HTTP 메서드입니다."),

  // DATABASE (DB 처리 관련)
  FAILED_TO_SAVE_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE001", "데이터 저장에 실패하였습니다."),
  FAILED_TO_LOAD_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE002", "데이터 로딩에 실패하였습니다."),
  FAILED_TO_UPDATE_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE003", "데이터 갱신에 실패하였습니다."),
  FAILED_TO_DELETE_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE004", "데이터 삭제에 실패하였습니다."),

  // Auth
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH001", "로그인에 실패했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
