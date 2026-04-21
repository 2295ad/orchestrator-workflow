package com.jswone.orchestrator.jobs.workflow.impl;

import com.jswone.orchestrator.jobs.activity.ConsumerActivity;
import com.jswone.orchestrator.jobs.workflow.OrchestratorSyncWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.Map;
import org.slf4j.Logger;

public class OrchestratorSyncWorkflowImpl implements OrchestratorSyncWorkflow {
  private static final Logger log = Workflow.getLogger(OrchestratorSyncWorkflowImpl.class);

  private static final int RETRY_INTERVAL = 10;
  private static final int MAX_ATTEMPTS = 5;

  private final ConsumerActivity activity =
      Workflow.newActivityStub(
          ConsumerActivity.class,
          ActivityOptions.newBuilder()
              .setStartToCloseTimeout(Duration.ofSeconds(30))
              .setScheduleToStartTimeout(
                  Duration.ofMinutes(2)) // worker should pickup before 2 mins
              .setScheduleToCloseTimeout(Duration.ofMinutes(3)) // max life cycle workflow limit
              .setRetryOptions(
                  RetryOptions.newBuilder()
                      .setInitialInterval(Duration.ofSeconds(RETRY_INTERVAL)) // retry after 10 sec
                      .setBackoffCoefficient(1) // backoff coeff
                      .setMaximumAttempts(MAX_ATTEMPTS) // 5 retries allowed, configurable
                      .build())
              .build());

  @Override
  public void initiateSyncWorkflow(Map<String, Object> payload) {
    log.info("workflow invoked");
    activity.verifyUser(payload);
    activity.transactionStatusUpdate((String) payload.get("transaction"));
  }
}
