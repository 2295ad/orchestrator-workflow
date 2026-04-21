package com.jswone.orchestrator.jobs.service.impl;

import com.jswone.orchestrator.jobs.service.OrchestrationService;
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
public class OrchestrationServiceImpl implements OrchestrationService {

  private final WorkflowClient workflowClient;

  @Value("${temporal.task-queue}")
  private String taskQueue;

  @Override
  public Boolean invokeWorkflow(Map<String, Object> payload) {
    try {
      WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(taskQueue).build();
      OrchestratorSyncWorkflow syncWorkflow =
          workflowClient.newWorkflowStub(OrchestratorSyncWorkflow.class, options);
      WorkflowClient.start(syncWorkflow::initiateSyncWorkflow, payload);
      log.info("initialised workflow for transaction id {}", payload.get("transaction"));
      return Boolean.TRUE;
    } catch (Exception e) {
      log.info("Exception in invoking workflow {}", e.getMessage());
      return Boolean.FALSE;
    }
  }
}
