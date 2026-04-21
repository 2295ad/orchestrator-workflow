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
public class ConsumerApi {

  @Value("${external-service.consumer.base-url}")
  private String consumerBaseUrl;

  @Value("${external-service.consumer.api-key}")
  private String consumerApiKey;

  private final RestTemplate restTemplate;
  private final ExternalApi externalApi;

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-API-KEY", this.consumerApiKey);
    return headers;
  }

  private <T, R> R httpCall(
      String url, HttpMethod method, T requestBody, ParameterizedTypeReference<R> responseType) {
    HttpEntity<T> httpEntity = new HttpEntity<>(requestBody, this.getHeaders());
    ResponseEntity<R> response = restTemplate.exchange(url, method, httpEntity, responseType);
    return response.getBody();
  }

  public ApiResponse<Map<String, Object>> updateTransactionStatus(Map<String, Object> data) {
    String url =
        consumerBaseUrl.concat(
            externalApi.getServices().get("account-master").get("update-customer"));
    log.info("customer details updated");
    return this.httpCall(url, HttpMethod.PUT, data, new ParameterizedTypeReference<>() {});
  }
}
