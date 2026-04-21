package com.jswone.orchestrator.http.filters;

import com.jswone.orchestrator.dto.constants.ServiceConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TraceIdFilter extends OncePerRequestFilter {

  public static final String TRACE_ID_HEADER = ServiceConstants.X_TRACE_ID;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String traceId = request.getHeader(TRACE_ID_HEADER);
    if (Objects.isNull(traceId) || traceId.isBlank()) {
      traceId = UUID.randomUUID().toString();
    }

    MDC.put(TRACE_ID_HEADER, traceId);

    response.setHeader(TRACE_ID_HEADER, traceId);

    try {
      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(TRACE_ID_HEADER);
    }
  }
}
