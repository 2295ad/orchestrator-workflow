package com.jswone.orchestrator.jobs.service.impl;

import com.jswone.orchestrator.jobs.service.SyncService;
import com.jswone.orchestrator.jobs.workflow.OrchestratorSyncWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncServiceImpl implements SyncService {
  private final WorkflowClient workflowClient;

  @Value("${temporal.task-queue}")
  private String taskQueue;

  @Override
  public Boolean initiateSyncWorkflow(Map<String, Object> payload) {
    log.info("Workflow to be triggered for transaction sync");
    WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(taskQueue).build();
    OrchestratorSyncWorkflow accountMasterSyncWorkflow =
        workflowClient.newWorkflowStub(OrchestratorSyncWorkflow.class, options);
    WorkflowClient.start(accountMasterSyncWorkflow::initiateSyncWorkflow, payload);
    log.info("initialised workflow for transaction sync {}", payload.get("transaction"));
    return Boolean.TRUE;
  }
}
