package com.jswone.orchestrator.http.rest;

import com.jswone.orchestrator.config.ExternalApi;
import com.jswone.orchestrator.dto.ApiResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserApi {

  @Value("${external-service.user.base-url}")
  private String userBaseUrl;

  @Value("${external-service.user.api-key}")
  private String userApiKey;

  private final RestTemplate restTemplate;
  private final ExternalApi externalApi;

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-API-KEY", this.userApiKey);
    return headers;
  }

  private <T, R> ApiResponse<R> httpCall(
      String url, HttpMethod method, T requestBody, ParameterizedTypeReference<R> responseType) {
    HttpEntity<T> httpEntity = new HttpEntity<>(requestBody, this.getHeaders());
    ResponseEntity<R> resp = restTemplate.exchange(url, method, httpEntity, responseType);
    return new ApiResponse<>((HttpStatus) resp.getStatusCode(), resp.getBody());
  }

  public ApiResponse<Map<String, Object>> fetchDetails(Map<String, Object> data) {
    String url = userBaseUrl.concat(externalApi.getServices().get("crawler").get("fetch-details"));
    log.info("verify user details");
    return this.httpCall(url, HttpMethod.POST, data, new ParameterizedTypeReference<>() {});
  }
}
