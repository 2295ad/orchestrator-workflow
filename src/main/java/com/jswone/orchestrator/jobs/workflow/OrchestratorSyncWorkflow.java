package com.jswone.orchestrator.jobs.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.Map;

@WorkflowInterface
public interface OrchestratorSyncWorkflow {
  @WorkflowMethod
  void initiateSyncWorkflow(Map<String, Object> payload);
}
