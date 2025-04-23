package com.example.monewteam08.config;

import com.example.monewteam08.exception.user.InvalidUserIdRequestHeaderFormatException;
import com.example.monewteam08.exception.user.MissingUserIdRequestHeaderException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticatedUserInterceptor implements HandlerInterceptor {

  private static final String HEADER_NAME = "Monew-Request-User-Id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String userId = request.getHeader(HEADER_NAME);

    if (userId == null || userId.isBlank()) {
      throw new MissingUserIdRequestHeaderException();
    }

    try {
      UUID.fromString(userId);
    } catch (IllegalArgumentException e) {
      throw new InvalidUserIdRequestHeaderFormatException();
    }
    return true;
  }
}
