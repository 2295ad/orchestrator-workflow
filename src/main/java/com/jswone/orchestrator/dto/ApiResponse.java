package com.jswone.orchestrator.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> {
  private final HttpStatus status;
  private final T body;
}
