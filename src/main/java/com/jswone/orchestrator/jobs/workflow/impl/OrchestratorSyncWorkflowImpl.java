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

  private final ConsumerActivity activity =
      Workflow.newActivityStub(
          ConsumerActivity.class,
          ActivityOptions.newBuilder()
              .setStartToCloseTimeout(Duration.ofSeconds(30))
              .setScheduleToStartTimeout(Duration.ofMinutes(2))
              .setScheduleToCloseTimeout(Duration.ofMinutes(3))
              .setRetryOptions(
                  RetryOptions.newBuilder()
                      .setInitialInterval(Duration.ofSeconds(5))
                      .setBackoffCoefficient(5)
                      .setMaximumAttempts(5)
                      .setMaximumInterval(Duration.ofSeconds(60))
                      .build())
              .build());

  @Override
  public void initiateSyncWorkflow(Map<String, Object> payload) {
    log.info("workflow invoked");
    activity.verifyUser();
    activity.transactionStatusUpdate((String) payload.get("transaction"));
  }
}
