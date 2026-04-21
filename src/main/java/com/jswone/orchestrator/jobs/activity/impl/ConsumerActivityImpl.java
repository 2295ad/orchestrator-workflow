package com.jswone.orchestrator.jobs.activity.impl;

import static com.jswone.orchestrator.dto.constants.ApiResponseConstants.*;

import com.jswone.orchestrator.dto.ApiResponse;
import com.jswone.orchestrator.http.rest.ConsumerApi;
import com.jswone.orchestrator.http.rest.UserApi;
import com.jswone.orchestrator.jobs.activity.ConsumerActivity;
import com.jswone.orchestrator.utility.CommonUtils;
import io.temporal.failure.ApplicationFailure;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerActivityImpl implements ConsumerActivity {
  private final ConsumerApi consumerApi;
  private final UserApi userApi;

  private final CommonUtils commonUtils;

  private void shouldAttemptRetry(ApiResponse<Map<String, Object>> apiResponse, String failureMsg) {
    Map<String, Object> response = apiResponse.getBody();
    int statusCode = apiResponse.getStatus().value();
    log.info("response {}", response);
    if (RETRYABLE_HTTP_CODES.contains(statusCode)) {
      throw ApplicationFailure.newFailure(commonUtils.convertToString(response), failureMsg);
    }
    throw ApplicationFailure.newNonRetryableFailure(
        commonUtils.convertToString(response), failureMsg);
  }

  @Override
  public Map<String, Object> transactionStatusUpdate(String transaction) {
    Map<String, Object> transactionStatusRequest = new HashMap<>();
    transactionStatusRequest.put("status", "SUCCESS");
    transactionStatusRequest.put("transaction", transaction);
    ApiResponse<Map<String, Object>> apiResponseObj =
        consumerApi.updateTransactionStatus(transactionStatusRequest);
    if (apiResponseObj.getStatus().is2xxSuccessful()
        && (Boolean) apiResponseObj.getBody().get("success")) {
      Map<String, Object> apiResponse = apiResponseObj.getBody();
      Map<String, Object> dataMap = (Map<String, Object>) apiResponse.get("data");
      return dataMap;
    }
    if (apiResponseObj.getStatus().is4xxClientError()) {
      log.info("invalid transaction");
      return null;
    }
    shouldAttemptRetry(apiResponseObj, "TX_STATUS_UPDATE_FAILED");
    return null;
  }

  @Override
  public Map<String, Object> verifyUser() {
    Map<String, Object> userReq = new HashMap<>();
    userReq.put("userId", "123");
    ApiResponse<Map<String, Object>> apiResponseObj = userApi.fetchDetails(userReq);
    if (apiResponseObj.getStatus().is2xxSuccessful()
        && (Boolean) apiResponseObj.getBody().get("success")) {
      Map<String, Object> apiResponse = apiResponseObj.getBody();
      return (Map<String, Object>) apiResponse.get("data");
    }
    if (apiResponseObj.getStatus().is4xxClientError()) {
      log.info("invalid user req");
      return null;
    }
    shouldAttemptRetry(apiResponseObj, "USER_API_FAILED");
    return null;
  }
}
