package com.example.monewteam08.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID = "requestId";
  private static final String METHOD = "method";
  private static final String URI = "uri";
  private static final String HEADER = "monew-Request-ID";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String requestId = UUID.randomUUID().toString();
    MDC.put(REQUEST_ID, requestId);
    MDC.put(METHOD, request.getMethod());
    MDC.put(URI, request.getRequestURI());

    response.setHeader(HEADER, requestId);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    MDC.clear();
  }
}
