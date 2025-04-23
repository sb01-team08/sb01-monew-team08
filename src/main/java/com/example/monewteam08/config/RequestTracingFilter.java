package com.example.monewteam08.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestTracingFilter extends OncePerRequestFilter {

  private static final String REQUEST_ID = "requestId";
  private static final String METHOD = "method";
  private static final String URI = "uri";
  private static final String HEADER_NAME = "Monew-Request-Id";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String requestId = UUID.randomUUID().toString().replace("-", "");

    response.setHeader(HEADER_NAME, requestId);

    MDC.put(REQUEST_ID, requestId);
    MDC.put(METHOD, request.getMethod());
    MDC.put(URI, request.getRequestURI());

    try {
      filterChain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
