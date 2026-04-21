package com.jswone.orchestrator.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonUtils {
  private final ObjectMapper objectMapper;

  public String convertToString(Map<String, Object> request) {
    try {
      return objectMapper.writeValueAsString(request);
    } catch (Exception e) {
      log.info("unable to cast request to string " + e.getMessage());
      return null;
    }
  }
}
