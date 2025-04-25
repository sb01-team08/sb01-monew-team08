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
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH001", "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요."),
  LOGIN_REJECTED_DELETED_USER(HttpStatus.UNAUTHORIZED, "AUTH002", "삭제된 계정은 로그인이 불가능합니다."),
  MISSING_USER_ID_HEADER(HttpStatus.UNAUTHORIZED, "AUTH003",
      "Monew-Request-User-Id가 필요합니다."),
  INVALID_USER_ID_HEADER_FORMAT(HttpStatus.BAD_REQUEST, "AUTH004",
      "Monew-Request-User-Id의 형식이 잘못되었습니다."),

  //댓글
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT001", "댓글을 찾을 수 없습니다."),
  UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "COMMENT002", "댓글을 수정/삭제 할 권한이 없습니다."),

  // User
  EMAIL_IS_ALREADY_EXIST(HttpStatus.CONFLICT, "USER001", "이미 사용 중인 이메일입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER002", "사용자를 찾을 수 없습니다."),

  // UserActivityLog
  USER_ACTIVITY_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "USERACTIVITY001", "사용자 활동 로그가 존재하지 않습니다."),

  // Article
  ARTICLE_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ARTICLE001", "기사 가져오기에 실패했습니다."),
  ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE002", "기사를 찾을 수 없습니다."),

  // Interest
  DUPLICATE_INTEREST(HttpStatus.CONFLICT, "INTEREST001", "이미 유사한 관심사가 존재합니다."),
  INTEREST_NOT_FOUND(HttpStatus.NOT_FOUND, "INTEREST002", "관심사를 찾을 수 없습니다."),

  // Subscription
  ALREADY_SUBSCRIPTION(HttpStatus.CONFLICT, "SUBSCRIPTION001", "이미 구독된 관심사입니다."),
  SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "SUBSCRIPTION002", "구독 정보를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
