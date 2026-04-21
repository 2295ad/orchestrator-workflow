package com.jswone.orchestrator.dto.constants;

import java.util.Set;

public class ApiResponseConstants {

  public static final Set<Integer> RETRYABLE_HTTP_CODES = Set.of(408, 429, 502, 503, 504);
}
